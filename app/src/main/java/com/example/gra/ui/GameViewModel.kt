package com.example.gra.ui

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gra.audio.SoundManager
import com.example.gra.engine.LevelManager
import com.example.gra.engine.MathParser
import com.example.gra.model.GameState
import com.example.gra.model.Projectile
import com.example.gra.model.Explosion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(LevelManager.loadLevel(1))
    val gameState = _gameState.asStateFlow()

    private val _cameraOffset = MutableStateFlow(Offset.Zero)
    val cameraOffset = _cameraOffset.asStateFlow()

    fun onFire(expression: String, direction: Int = 1) {
        SoundManager.play(SoundManager.SoundEffect.SHOOT)

        val current = _gameState.value
        val points = mutableListOf<Offset>()
        var hitTarget = false
        var hitObstacleIndex: Int? = null
        var hitPoint: Offset? = null

        // Validate expression first
        if (expression.isBlank()) return

        // Calculate Y offset at x=0 to ensure trajectory starts from player
        val yOffsetAtZero = MathParser.eval(expression, 0.0) ?: return

        // Calculate trajectory with fine steps for accuracy
        val maxSteps = 400 // Increased for better accuracy
        val stepSize = 0.25 // Smaller steps = better accuracy

        for (i in 0..maxSteps) {
            val x = i * stepSize * direction
            val rawY = MathParser.eval(expression, x)

            if (rawY != null && !rawY.isNaN() && !rawY.isInfinite()) {
                // Apply Y correction so trajectory starts at player position
                // Mathematical Y+ is UP, so we ADD correctedY
                val correctedY = rawY - yOffsetAtZero

                // World coordinates (player-relative)
                // In world coords: Y+ is mathematically UP
                val worldPoint = Offset(
                    current.playerPos.x + x.toFloat(),
                    current.playerPos.y + correctedY.toFloat()
                )
                points.add(worldPoint)

                // Check bounds - stop at field boundaries
                val bounds = current.fieldBounds
                if (worldPoint.x < bounds.minX || worldPoint.x > bounds.maxX ||
                    worldPoint.y < bounds.minY || worldPoint.y > bounds.maxY) {
                    break
                }

                // Collision with Target (radius ~1.5 units)
                val dx = worldPoint.x - current.targetPos.x
                val dy = worldPoint.y - current.targetPos.y
                val distanceToTarget = kotlin.math.sqrt(dx * dx + dy * dy)

                if (distanceToTarget < 1.5f) {
                    hitTarget = true
                    break
                }

                // Collision with Obstacles
                for ((index, obstacle) in current.obstacles.withIndex()) {
                    if (obstacle.hasCollision(worldPoint)) {
                        hitObstacleIndex = index
                        hitPoint = worldPoint
                        break
                    }
                }

                if (hitObstacleIndex != null) {
                    break
                }
            } else {
                // If function becomes invalid, stop trajectory
                break
            }
        }

        // Handle hit target
        if (hitTarget) {
            viewModelScope.launch {
                animateProjectile(points)
                SoundManager.play(SoundManager.SoundEffect.HIT)
                delay(400)
                _gameState.value = current.copy(
                    showVictoryDialog = true,
                    projectiles = emptyList(),
                    shotsFired = current.shotsFired + 1
                )
                SoundManager.play(SoundManager.SoundEffect.LEVEL_COMPLETE)
            }
            return
        }

        // Handle hit obstacle
        if (hitObstacleIndex != null && hitPoint != null) {
            val updatedObstacles = current.obstacles.mapIndexed { index, obstacle ->
                if (index == hitObstacleIndex) {
                    obstacle.addHole(hitPoint)
                } else {
                    obstacle
                }
            }

            viewModelScope.launch {
                animateProjectile(points)
                SoundManager.play(SoundManager.SoundEffect.EXPLOSION)

                // Add explosion effect
                val explosion = Explosion(position = hitPoint)
                _gameState.value = current.copy(
                    obstacles = updatedObstacles,
                    explosions = current.explosions + explosion,
                    shotsFired = current.shotsFired + 1
                )

                delay(300)
                _gameState.value = _gameState.value.copy(projectiles = emptyList())

                // Clean up expired explosions
                cleanupExplosions()
            }
            return
        }

        // No hit - trajectory reaches bounds or ends naturally
        if (points.isNotEmpty()) {
            viewModelScope.launch {
                animateProjectile(points)
                delay(300)
                _gameState.value = current.copy(
                    projectiles = emptyList(),
                    shotsFired = current.shotsFired + 1
                )
            }
        }
    }

    private suspend fun animateProjectile(points: List<Offset>) {
        val step = maxOf(1, points.size / 80) // Show ~80 frames for very smooth animation
        for (i in step..points.size step step) {
            _gameState.value = _gameState.value.copy(
                projectiles = listOf(Projectile(points.take(i), i.toFloat() / points.size))
            )
            delay(30) // Much slower animation - 30ms per frame
        }
        delay(300)
    }

    fun loadLevel(level: Int) {
        _gameState.value = LevelManager.loadLevel(level).copy(showVictoryDialog = false)
        centerOnPlayer()
    }

    private fun nextLevel() {
        val currentLevel = _gameState.value.currentLevel
        if (currentLevel < 30) {
            _gameState.value = LevelManager.loadLevel(currentLevel + 1)
            centerOnPlayer()
        } else {
            // Game Won!
        }
    }

    fun onMove(expression: String, deltaX: Float) {
        val current = _gameState.value
        if (current.moveCharges > 0) {
            // Validate expression
            if (expression.isBlank()) return

            // Calculate Y offset at x=0
            val yOffsetAtZero = MathParser.eval(expression, 0.0) ?: return

            // Calculate Y at deltaX with correction
            val rawY = MathParser.eval(expression, deltaX.toDouble())
            if (rawY != null && !rawY.isNaN() && !rawY.isInfinite()) {
                val correctedY = rawY - yOffsetAtZero

                SoundManager.play(SoundManager.SoundEffect.MOVE)

                // World coordinates: Y+ is mathematically UP
                val newPos = Offset(
                    current.playerPos.x + deltaX,
                    current.playerPos.y + correctedY.toFloat()
                )

                // Check if new position is within bounds
                val bounds = current.fieldBounds
                if (newPos.x >= bounds.minX && newPos.x <= bounds.maxX &&
                    newPos.y >= bounds.minY && newPos.y <= bounds.maxY) {
                    _gameState.value = current.copy(
                        playerPos = newPos,
                        moveCharges = current.moveCharges - 1
                    )
                }
            }
        }
    }

    fun updateCameraOffset(offset: Offset) {
        _cameraOffset.value = offset
    }

    fun centerOnPlayer() {
        _cameraOffset.value = Offset.Zero
    }

    private fun cleanupExplosions() {
        viewModelScope.launch {
            while (true) {
                delay(100)
                val current = _gameState.value
                val activeExplosions = current.explosions.filter { it.isActive }
                if (activeExplosions.size != current.explosions.size) {
                    _gameState.value = current.copy(explosions = activeExplosions)
                }
                if (activeExplosions.isEmpty()) break
            }
        }
    }
}
