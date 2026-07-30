package com.example.gra.model

import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

data class GameState(
    val playerPos: Offset,
    val targetPos: Offset,
    val obstacles: List<Obstacle> = emptyList(),
    val moveCharges: Int = 3,
    val currentLevel: Int = 1,
    val isGameOver: Boolean = false,
    val projectiles: List<Projectile> = emptyList(),
    val fieldBounds: FieldBounds = FieldBounds(),
    val shotsFired: Int = 0,
    val explosions: List<Explosion> = emptyList(),
    val showVictoryDialog: Boolean = false
)

data class Obstacle(
    val center: Offset,
    val radius: Float,
    val holes: List<Hole> = emptyList()
) {
    val destroyed: Boolean get() = holes.size >= 15

    fun distanceFromCenter(point: Offset): Float {
        return sqrt(
            (point.x - center.x) * (point.x - center.x) +
            (point.y - center.y) * (point.y - center.y)
        )
    }

    fun isPointInside(point: Offset): Boolean {
        return distanceFromCenter(point) <= radius
    }

    fun hasCollision(point: Offset): Boolean {
        if (!isPointInside(point)) return false
        if (destroyed) return false

        // Check if point is inside any hole
        for (hole in holes) {
            val distToHole = sqrt(
                (point.x - hole.center.x) * (point.x - hole.center.x) +
                (point.y - hole.center.y) * (point.y - hole.center.y)
            )
            if (distToHole <= hole.radius) {
                return false // Inside a hole, no collision
            }
        }

        return true // Inside obstacle and not in any hole
    }

    fun addHole(hitPoint: Offset): Obstacle {
        if (destroyed) return this
        if (!isPointInside(hitPoint)) return this

        // Check if hit point is near existing hole
        val nearbyHole = holes.find { hole ->
            val dist = sqrt(
                (hitPoint.x - hole.center.x) * (hitPoint.x - hole.center.x) +
                (hitPoint.y - hole.center.y) * (hitPoint.y - hole.center.y)
            )
            dist < hole.radius * 1.2f
        }

        return if (nearbyHole != null) {
            // Expand existing hole
            val expandedHoles = holes.map { hole ->
                if (hole == nearbyHole) {
                    hole.copy(radius = (hole.radius + 0.4f).coerceAtMost(radius * 0.5f))
                } else {
                    hole
                }
            }
            copy(holes = expandedHoles)
        } else {
            // Create new hole
            copy(holes = holes + Hole(center = hitPoint, radius = 0.7f))
        }
    }
}

data class Hole(
    val center: Offset,
    val radius: Float = 0.7f
)

data class Projectile(
    val points: List<Offset>,
    val progress: Float = 0f
)

data class Explosion(
    val position: Offset,
    val startTime: Long = System.currentTimeMillis(),
    val duration: Long = 500
) {
    val isActive: Boolean
        get() = System.currentTimeMillis() - startTime < duration

    val progress: Float
        get() = ((System.currentTimeMillis() - startTime).toFloat() / duration).coerceIn(0f, 1f)
}

data class FieldBounds(
    val minX: Float = -100f,
    val maxX: Float = 100f,
    val minY: Float = -60f,
    val maxY: Float = 60f
)
