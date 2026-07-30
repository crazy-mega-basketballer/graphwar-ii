package com.example.gra.engine

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.example.gra.model.GameState
import com.example.gra.model.Obstacle
import com.example.gra.model.FieldBounds

object LevelManager {
    fun loadLevel(level: Int): GameState {
        return when (level) {
            1 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(15f, 0f),
                obstacles = emptyList(),
                level = 1
            )
            2 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(20f, 5f),
                obstacles = emptyList(),
                level = 2
            )
            3 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(25f, -8f),
                obstacles = emptyList(),
                level = 3
            )
            4 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(20f, 0f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(10f, -5f), Size(2f, 10f)))
                ),
                level = 4
            )
            5 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(30f, 5f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(15f, -3f), Size(2f, 12f)))
                ),
                level = 5
            )
            6 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(25f, -10f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(12f, -8f), Size(2f, 12f))),
                    Obstacle(Rect(Offset(18f, -6f), Size(2f, 10f)))
                ),
                level = 6
            )
            7 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(28f, 0f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(10f, -4f), Size(1.5f, 8f))),
                    Obstacle(Rect(Offset(20f, -4f), Size(1.5f, 8f)))
                ),
                level = 7
            )
            8 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(35f, 8f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(18f, -5f), Size(3f, 18f)))
                ),
                level = 8
            )
            9 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(30f, -12f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(15f, -10f), Size(2f, 12f))),
                    Obstacle(Rect(Offset(22f, -8f), Size(2f, 10f)))
                ),
                level = 9
            )
            10 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(32f, 0f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(10f, -4f), Size(1.5f, 8f))),
                    Obstacle(Rect(Offset(18f, -4f), Size(1.5f, 8f))),
                    Obstacle(Rect(Offset(26f, -4f), Size(1.5f, 8f)))
                ),
                level = 10
            )
            11 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-20f, 5f),
                obstacles = emptyList(),
                level = 11
            )
            12 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-25f, -6f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-15f, -5f), Size(2f, 12f)))
                ),
                level = 12
            )
            13 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(28f, -15f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(15f, -20f), Size(4f, 25f)))
                ),
                level = 13
            )
            14 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(35f, 6f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(15f, -5f), Size(2f, 15f))),
                    Obstacle(Rect(Offset(22f, -4f), Size(2f, 13f))),
                    Obstacle(Rect(Offset(28f, -3f), Size(1.5f, 12f)))
                ),
                level = 14
            )
            15 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(30f, 0f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(10f, -4f), Size(1.5f, 8f))),
                    Obstacle(Rect(Offset(17f, -4f), Size(1.5f, 8f))),
                    Obstacle(Rect(Offset(24f, -4f), Size(1.5f, 8f)))
                ),
                level = 15
            )
            16 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(40f, -12f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(20f, -18f), Size(3f, 20f))),
                    Obstacle(Rect(Offset(30f, -15f), Size(2.5f, 18f)))
                ),
                level = 16
            )
            17 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-30f, 8f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-22f, 0f), Size(2f, 12f))),
                    Obstacle(Rect(Offset(-12f, 2f), Size(1.5f, 10f)))
                ),
                level = 17
            )
            18 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(38f, -18f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(15f, -22f), Size(2.5f, 25f))),
                    Obstacle(Rect(Offset(25f, -18f), Size(2f, 20f))),
                    Obstacle(Rect(Offset(33f, -16f), Size(1.5f, 18f)))
                ),
                level = 18
            )
            19 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(32f, -14f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(12f, -12f), Size(3f, 18f))),
                    Obstacle(Rect(Offset(20f, -16f), Size(2.5f, 20f))),
                    Obstacle(Rect(Offset(27f, -14f), Size(2f, 18f)))
                ),
                level = 19
            )
            20 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(45f, 5f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(15f, -8f), Size(2f, 18f))),
                    Obstacle(Rect(Offset(23f, -6f), Size(2.5f, 16f))),
                    Obstacle(Rect(Offset(31f, -5f), Size(2f, 15f))),
                    Obstacle(Rect(Offset(38f, -4f), Size(1.5f, 13f)))
                ),
                level = 20
            )
            21 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-35f, -10f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-28f, -12f), Size(2.5f, 16f))),
                    Obstacle(Rect(Offset(-18f, -10f), Size(2f, 14f))),
                    Obstacle(Rect(Offset(-8f, -8f), Size(1.5f, 12f)))
                ),
                level = 21
            )
            22 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(40f, -20f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(15f, -28f), Size(3f, 30f))),
                    Obstacle(Rect(Offset(25f, -24f), Size(2.5f, 26f))),
                    Obstacle(Rect(Offset(34f, -22f), Size(2f, 24f)))
                ),
                level = 22
            )
            23 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(35f, 0f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(12f, -4f), Size(1.5f, 8f))),
                    Obstacle(Rect(Offset(18f, -4f), Size(1.5f, 8f))),
                    Obstacle(Rect(Offset(24f, -4f), Size(1.5f, 8f))),
                    Obstacle(Rect(Offset(30f, -4f), Size(1.5f, 8f)))
                ),
                level = 23
            )
            24 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-40f, 8f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-32f, 0f), Size(2.5f, 14f))),
                    Obstacle(Rect(Offset(-22f, -2f), Size(2f, 16f))),
                    Obstacle(Rect(Offset(-12f, 0f), Size(1.5f, 14f)))
                ),
                level = 24
            )
            25 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(48f, -14f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(15f, -20f), Size(3f, 26f))),
                    Obstacle(Rect(Offset(25f, -18f), Size(2.5f, 24f))),
                    Obstacle(Rect(Offset(34f, -16f), Size(2f, 22f))),
                    Obstacle(Rect(Offset(42f, -14f), Size(1.5f, 20f)))
                ),
                level = 25
            )
            26 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(40f, -22f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(15f, -30f), Size(2.5f, 32f))),
                    Obstacle(Rect(Offset(24f, -26f), Size(3f, 30f))),
                    Obstacle(Rect(Offset(32f, -24f), Size(2.5f, 28f)))
                ),
                level = 26
            )
            27 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-45f, -12f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-38f, -18f), Size(3f, 24f))),
                    Obstacle(Rect(Offset(-28f, -16f), Size(2.5f, 22f))),
                    Obstacle(Rect(Offset(-18f, -14f), Size(2f, 20f))),
                    Obstacle(Rect(Offset(-8f, -12f), Size(1.5f, 18f)))
                ),
                level = 27
            )
            28 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(52f, -18f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(15f, -26f), Size(3.5f, 32f))),
                    Obstacle(Rect(Offset(26f, -24f), Size(3f, 30f))),
                    Obstacle(Rect(Offset(36f, -22f), Size(2.5f, 28f))),
                    Obstacle(Rect(Offset(45f, -20f), Size(2f, 26f)))
                ),
                level = 28
            )
            29 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(45f, 0f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(12f, -5f), Size(2f, 10f))),
                    Obstacle(Rect(Offset(19f, -5f), Size(2f, 10f))),
                    Obstacle(Rect(Offset(26f, -5f), Size(2f, 10f))),
                    Obstacle(Rect(Offset(33f, -5f), Size(2f, 10f))),
                    Obstacle(Rect(Offset(40f, -5f), Size(2f, 10f)))
                ),
                level = 29
            )
            30 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-50f, -20f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-44f, -32f), Size(3.5f, 36f))),
                    Obstacle(Rect(Offset(-35f, -30f), Size(3f, 34f))),
                    Obstacle(Rect(Offset(-26f, -28f), Size(2.5f, 32f))),
                    Obstacle(Rect(Offset(-17f, -26f), Size(2f, 30f))),
                    Obstacle(Rect(Offset(-8f, -24f), Size(1.5f, 28f)))
                ),
                level = 30
            )
            else -> loadLevel(1)
        }
    }

    private fun createLevel(
        playerPos: Offset,
        targetPos: Offset,
        obstacles: List<Obstacle>,
        level: Int
    ): GameState {
        // Calculate field bounds based on level elements
        val allX = listOf(playerPos.x, targetPos.x) +
                   obstacles.flatMap { listOf(it.rect.left, it.rect.right) }
        val allY = listOf(playerPos.y, targetPos.y) +
                   obstacles.flatMap { listOf(it.rect.top, it.rect.bottom) }

        val bounds = FieldBounds(
            minX = (allX.minOrNull() ?: 0f) - 20f,
            maxX = (allX.maxOrNull() ?: 0f) + 20f,
            minY = (allY.minOrNull() ?: 0f) - 15f,
            maxY = (allY.maxOrNull() ?: 0f) + 15f
        )

        return GameState(
            playerPos = playerPos,
            targetPos = targetPos,
            obstacles = obstacles,
            currentLevel = level,
            moveCharges = 3,
            fieldBounds = bounds
        )
    }
}
