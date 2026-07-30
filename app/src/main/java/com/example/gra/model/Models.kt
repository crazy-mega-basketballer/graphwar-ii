package com.example.gra.model

import androidx.compose.ui.geometry.Offset

data class GameState(
    val playerPos: Offset,
    val targetPos: Offset,
    val obstacles: List<Obstacle> = emptyList(),
    val moveCharges: Int = 3,
    val currentLevel: Int = 1,
    val isGameOver: Boolean = false,
    val projectiles: List<Projectile> = emptyList(),
    val fieldBounds: FieldBounds = FieldBounds()
)

data class Obstacle(
    val rect: androidx.compose.ui.geometry.Rect,
    val maxHealth: Int = 3,
    val currentHealth: Int = 3
) {
    val destroyed: Boolean get() = currentHealth <= 0
    val healthPercent: Float get() = currentHealth.toFloat() / maxHealth.toFloat()
}

data class Projectile(
    val points: List<Offset>,
    val progress: Float = 0f
)

data class FieldBounds(
    val minX: Float = -50f,
    val maxX: Float = 50f,
    val minY: Float = -30f,
    val maxY: Float = 30f
)
