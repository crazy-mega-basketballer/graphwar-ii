package com.example.gra.model

import androidx.compose.ui.geometry.Offset

data class GameState(
    val playerPos: Offset,
    val targetPos: Offset,
    val obstacles: List<Obstacle> = emptyList(),
    val moveCharges: Int = 3,
    val currentLevel: Int = 1,
    val isGameOver: Boolean = false,
    val projectiles: List<Projectile> = emptyList()
)

data class Obstacle(
    val rect: androidx.compose.ui.geometry.Rect,
    val destroyed: Boolean = false
)

data class Projectile(
    val points: List<Offset>,
    val progress: Float = 0f
)
