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
        var destroyedObstacles = mutableListOf<Int>()

        // Calculate Y offset at x=0 to ensure trajectory starts from player
        val yOffsetAtZero = MathParser.eval(expression, 0.0) ?: 0.0

        // Calculate trajectory in both directions
        val range = if (direction > 0) {
            (0..100).map { it * 0.5 } // 0, 0.5, 1.0, 1.5, ... 50
        } else {
            (0..100).map { it * -0.5 } // 0, -0.5, -1.0, -1.5, ... -50
        }

        for (x in range) {
            val rawY = MathParser.eval(expression, x)
            if (rawY != null) {
                // Apply Y correction so trajectory starts at player position
                val correctedY = rawY - yOffsetAtZero

                // World coordinates (relative to player)
                val worldPoint = Offset(
                    current.playerPos.x + x.toFloat(),
                    current.playerPos.y - correctedY.toFloat()
                )
                points.add(worldPoint)

                // Check bounds
                val bounds = current.fieldBounds
                if (worldPoint.x < bounds.minX || worldPoint.x > bounds.maxX ||
                    worldPoint.y < bounds.minY || worldPoint.y > bounds.maxY) {
                    break
                }

                // Collision with Target (radius ~1 unit)
                if ((worldPoint - current.targetPos).getDistance() < 1.5f) {
                    hitTarget = true
                    viewModelScope.launch {
                        animateProjectile(points)
                        SoundManager.play(SoundManager.SoundEffect.HIT)
                        delay(300)
                        SoundManager.play(SoundManager.SoundEffect.LEVEL_COMPLETE)
                        nextLevel()
                    }
                    break
                }

                // Collision with Obstacles
                current.obstacles.forEachIndexed { index, obstacle ->
                    if (obstacle.rect.contains(worldPoint) && !obstacle.destroyed) {
                        if (index !in destroyedObstacles) {
                            destroyedObstacles.add(index)
                        }
                    }
                }

                // Stop if hit non-destroyed obstacle
                if (current.obstacles.any { it.rect.contains(worldPoint) && !it.destroyed }) {
                    if (destroyedObstacles.isNotEmpty()) {
                        SoundManager.play(SoundManager.SoundEffect.EXPLOSION)
                    }
                    break
                }
            } else break
        }

        if (points.isNotEmpty()) {
            val updatedObstacles = current.obstacles.mapIndexed { index, obstacle ->
                if (index in destroyedObstacles) obstacle.copy(destroyed = true) else obstacle
            }

            viewModelScope.launch {
                animateProjectile(points)
                _gameState.value = current.copy(obstacles = updatedObstacles)
            }
        }
    }

    private suspend fun animateProjectile(points: List<Offset>) {
        val current = _gameState.value
        val step = maxOf(1, points.size / 50) // Show ~50 frames max
        for (i in step..points.size step step) {
            _gameState.value = current.copy(
                projectiles = listOf(Projectile(points.take(i), i.toFloat() / points.size))
            )
            delay(10)
        }
        delay(300)
        _gameState.value = current.copy(projectiles = emptyList())
    }

    fun loadLevel(level: Int) {
        _gameState.value = LevelManager.loadLevel(level)
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
            // Calculate Y offset at x=0
            val yOffsetAtZero = MathParser.eval(expression, 0.0) ?: 0.0

            // Calculate Y at deltaX with correction
            val rawY = MathParser.eval(expression, deltaX.toDouble())
            if (rawY != null) {
                val correctedY = rawY - yOffsetAtZero

                SoundManager.play(SoundManager.SoundEffect.MOVE)
                val newPos = Offset(
                    current.playerPos.x + deltaX,
                    current.playerPos.y - correctedY.toFloat()
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
}
