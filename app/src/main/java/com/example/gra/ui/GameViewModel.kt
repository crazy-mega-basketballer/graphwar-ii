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

    fun onFire(expression: String, direction: Int = 1) {
        SoundManager.play(SoundManager.SoundEffect.SHOOT)

        val current = _gameState.value
        val points = mutableListOf<Offset>()
        var hitTarget = false
        var destroyedObstacles = mutableListOf<Int>()

        // Calculate trajectory (relative to player) in both directions
        val range = if (direction > 0) (0..2000 step 5) else (0 downTo -2000 step 5)

        for (x in range) {
            val y = MathParser.eval(expression, x.toDouble())
            if (y != null) {
                // World coordinates: x goes right, y goes up (inverted from screen coords)
                val worldPoint = Offset(
                    current.playerPos.x + x.toFloat(),
                    current.playerPos.y - y.toFloat()
                )
                points.add(worldPoint)

                // Collision with Target
                if ((worldPoint - current.targetPos).getDistance() < 25f) {
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

                // Out of bounds
                if (worldPoint.x > 3000 || worldPoint.x < -1000 || worldPoint.y > 3000 || worldPoint.y < -1000) break
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
        for (i in 1..points.size step 2) {
            _gameState.value = current.copy(
                projectiles = listOf(Projectile(points.take(i), i.toFloat() / points.size))
            )
            delay(3) // Smooth, fast animation
        }
        delay(300)
        _gameState.value = current.copy(projectiles = emptyList())
    }

    fun loadLevel(level: Int) {
        _gameState.value = LevelManager.loadLevel(level)
    }

    private fun nextLevel() {
        val currentLevel = _gameState.value.currentLevel
        if (currentLevel < 30) {
            _gameState.value = LevelManager.loadLevel(currentLevel + 1)
        } else {
            // Game Won!
        }
    }

    fun onMove(expression: String, deltaX: Float) {
        val current = _gameState.value
        if (current.moveCharges > 0) {
            val y = MathParser.eval(expression, deltaX.toDouble())
            if (y != null) {
                SoundManager.play(SoundManager.SoundEffect.MOVE)
                val newPos = Offset(current.playerPos.x + deltaX, current.playerPos.y - y.toFloat())
                _gameState.value = current.copy(
                    playerPos = newPos,
                    moveCharges = current.moveCharges - 1
                )
            }
        }
    }
}
