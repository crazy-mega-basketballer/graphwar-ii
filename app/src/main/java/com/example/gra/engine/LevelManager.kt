package com.example.gra.engine

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.example.gra.model.GameState
import com.example.gra.model.Obstacle

object LevelManager {
    fun loadLevel(level: Int): GameState {
        return when (level) {
            1 -> createLevel(
                playerPos = Offset(100f, 500f),
                targetPos = Offset(400f, 500f),
                obstacles = emptyList(),
                level = 1
            )
            2 -> createLevel(
                playerPos = Offset(100f, 500f),
                targetPos = Offset(500f, 400f),
                obstacles = emptyList(),
                level = 2
            )
            3 -> createLevel(
                playerPos = Offset(100f, 500f),
                targetPos = Offset(600f, 300f),
                obstacles = emptyList(),
                level = 3
            )
            4 -> createLevel(
                playerPos = Offset(100f, 500f),
                targetPos = Offset(500f, 500f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(300f, 400f), Size(50f, 200f)))
                ),
                level = 4
            )
            5 -> createLevel(
                playerPos = Offset(100f, 500f),
                targetPos = Offset(700f, 400f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(400f, 350f), Size(60f, 250f)))
                ),
                level = 5
            )
            6 -> createLevel(
                playerPos = Offset(150f, 500f),
                targetPos = Offset(650f, 300f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(350f, 300f), Size(70f, 300f))),
                    Obstacle(Rect(Offset(500f, 400f), Size(50f, 200f)))
                ),
                level = 6
            )
            7 -> createLevel(
                playerPos = Offset(100f, 600f),
                targetPos = Offset(700f, 600f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(300f, 500f), Size(50f, 200f))),
                    Obstacle(Rect(Offset(500f, 500f), Size(50f, 200f)))
                ),
                level = 7
            )
            8 -> createLevel(
                playerPos = Offset(100f, 500f),
                targetPos = Offset(800f, 350f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(400f, 250f), Size(80f, 400f)))
                ),
                level = 8
            )
            9 -> createLevel(
                playerPos = Offset(100f, 600f),
                targetPos = Offset(700f, 300f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(350f, 350f), Size(60f, 300f))),
                    Obstacle(Rect(Offset(550f, 250f), Size(60f, 200f)))
                ),
                level = 9
            )
            10 -> createLevel(
                playerPos = Offset(150f, 550f),
                targetPos = Offset(750f, 550f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(300f, 450f), Size(50f, 200f))),
                    Obstacle(Rect(Offset(450f, 450f), Size(50f, 200f))),
                    Obstacle(Rect(Offset(600f, 450f), Size(50f, 200f)))
                ),
                level = 10
            )
            11 -> createLevel(
                playerPos = Offset(100f, 500f),
                targetPos = Offset(-300f, 400f), // Target to the left!
                obstacles = emptyList(),
                level = 11
            )
            12 -> createLevel(
                playerPos = Offset(100f, 500f),
                targetPos = Offset(-400f, 350f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-200f, 350f), Size(60f, 250f)))
                ),
                level = 12
            )
            13 -> createLevel(
                playerPos = Offset(100f, 600f),
                targetPos = Offset(650f, 200f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(300f, 200f), Size(100f, 500f)))
                ),
                level = 13
            )
            14 -> createLevel(
                playerPos = Offset(100f, 500f),
                targetPos = Offset(800f, 400f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(300f, 300f), Size(60f, 300f))),
                    Obstacle(Rect(Offset(500f, 350f), Size(60f, 250f))),
                    Obstacle(Rect(Offset(650f, 300f), Size(50f, 300f)))
                ),
                level = 14
            )
            15 -> createLevel(
                playerPos = Offset(100f, 650f),
                targetPos = Offset(700f, 650f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(250f, 550f), Size(50f, 200f))),
                    Obstacle(Rect(Offset(400f, 550f), Size(50f, 200f))),
                    Obstacle(Rect(Offset(550f, 550f), Size(50f, 200f)))
                ),
                level = 15
            )
            16 -> createLevel(
                playerPos = Offset(100f, 500f),
                targetPos = Offset(900f, 300f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(400f, 200f), Size(80f, 400f))),
                    Obstacle(Rect(Offset(650f, 250f), Size(70f, 350f)))
                ),
                level = 16
            )
            17 -> createLevel(
                playerPos = Offset(150f, 550f),
                targetPos = Offset(-500f, 450f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-350f, 400f), Size(60f, 250f))),
                    Obstacle(Rect(Offset(-150f, 450f), Size(50f, 200f)))
                ),
                level = 17
            )
            18 -> createLevel(
                playerPos = Offset(100f, 600f),
                targetPos = Offset(850f, 250f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(300f, 250f), Size(70f, 450f))),
                    Obstacle(Rect(Offset(550f, 200f), Size(60f, 300f))),
                    Obstacle(Rect(Offset(750f, 150f), Size(50f, 250f)))
                ),
                level = 18
            )
            19 -> createLevel(
                playerPos = Offset(100f, 700f),
                targetPos = Offset(700f, 300f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(250f, 400f), Size(80f, 400f))),
                    Obstacle(Rect(Offset(450f, 250f), Size(70f, 300f))),
                    Obstacle(Rect(Offset(600f, 200f), Size(60f, 250f)))
                ),
                level = 19
            )
            20 -> createLevel(
                playerPos = Offset(100f, 600f),
                targetPos = Offset(1000f, 400f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(300f, 300f), Size(60f, 400f))),
                    Obstacle(Rect(Offset(500f, 350f), Size(70f, 350f))),
                    Obstacle(Rect(Offset(700f, 300f), Size(60f, 300f))),
                    Obstacle(Rect(Offset(850f, 350f), Size(50f, 250f)))
                ),
                level = 20
            )
            21 -> createLevel(
                playerPos = Offset(100f, 500f),
                targetPos = Offset(-600f, 350f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-450f, 300f), Size(70f, 300f))),
                    Obstacle(Rect(Offset(-250f, 350f), Size(60f, 250f))),
                    Obstacle(Rect(Offset(-50f, 400f), Size(50f, 200f)))
                ),
                level = 21
            )
            22 -> createLevel(
                playerPos = Offset(100f, 700f),
                targetPos = Offset(900f, 200f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(300f, 200f), Size(80f, 600f))),
                    Obstacle(Rect(Offset(550f, 150f), Size(70f, 400f))),
                    Obstacle(Rect(Offset(750f, 100f), Size(60f, 350f)))
                ),
                level = 22
            )
            23 -> createLevel(
                playerPos = Offset(100f, 600f),
                targetPos = Offset(800f, 600f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(250f, 500f), Size(50f, 250f))),
                    Obstacle(Rect(Offset(400f, 500f), Size(50f, 250f))),
                    Obstacle(Rect(Offset(550f, 500f), Size(50f, 250f))),
                    Obstacle(Rect(Offset(700f, 500f), Size(50f, 250f)))
                ),
                level = 23
            )
            24 -> createLevel(
                playerPos = Offset(150f, 550f),
                targetPos = Offset(-700f, 400f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-550f, 350f), Size(70f, 300f))),
                    Obstacle(Rect(Offset(-350f, 300f), Size(60f, 350f))),
                    Obstacle(Rect(Offset(-150f, 350f), Size(50f, 300f)))
                ),
                level = 24
            )
            25 -> createLevel(
                playerPos = Offset(100f, 700f),
                targetPos = Offset(1100f, 300f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(300f, 250f), Size(80f, 550f))),
                    Obstacle(Rect(Offset(550f, 200f), Size(70f, 400f))),
                    Obstacle(Rect(Offset(750f, 250f), Size(60f, 350f))),
                    Obstacle(Rect(Offset(950f, 200f), Size(50f, 300f)))
                ),
                level = 25
            )
            26 -> createLevel(
                playerPos = Offset(100f, 800f),
                targetPos = Offset(900f, 200f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(300f, 300f), Size(70f, 600f))),
                    Obstacle(Rect(Offset(500f, 150f), Size(80f, 500f))),
                    Obstacle(Rect(Offset(700f, 100f), Size(70f, 450f)))
                ),
                level = 26
            )
            27 -> createLevel(
                playerPos = Offset(100f, 650f),
                targetPos = Offset(-800f, 350f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-650f, 300f), Size(80f, 450f))),
                    Obstacle(Rect(Offset(-450f, 250f), Size(70f, 400f))),
                    Obstacle(Rect(Offset(-250f, 300f), Size(60f, 350f))),
                    Obstacle(Rect(Offset(-50f, 350f), Size(50f, 300f)))
                ),
                level = 27
            )
            28 -> createLevel(
                playerPos = Offset(100f, 750f),
                targetPos = Offset(1200f, 250f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(300f, 200f), Size(90f, 650f))),
                    Obstacle(Rect(Offset(550f, 150f), Size(80f, 500f))),
                    Obstacle(Rect(Offset(800f, 200f), Size(70f, 450f))),
                    Obstacle(Rect(Offset(1000f, 150f), Size(60f, 400f)))
                ),
                level = 28
            )
            29 -> createLevel(
                playerPos = Offset(100f, 700f),
                targetPos = Offset(1000f, 700f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(250f, 600f), Size(60f, 300f))),
                    Obstacle(Rect(Offset(400f, 600f), Size(60f, 300f))),
                    Obstacle(Rect(Offset(550f, 600f), Size(60f, 300f))),
                    Obstacle(Rect(Offset(700f, 600f), Size(60f, 300f))),
                    Obstacle(Rect(Offset(850f, 600f), Size(60f, 300f)))
                ),
                level = 29
            )
            30 -> createLevel(
                playerPos = Offset(100f, 800f),
                targetPos = Offset(-1000f, 200f),
                obstacles = listOf(
                    Obstacle(Rect(Offset(-850f, 200f), Size(90f, 700f))),
                    Obstacle(Rect(Offset(-650f, 150f), Size(80f, 600f))),
                    Obstacle(Rect(Offset(-450f, 200f), Size(70f, 550f))),
                    Obstacle(Rect(Offset(-250f, 150f), Size(60f, 500f))),
                    Obstacle(Rect(Offset(-50f, 200f), Size(50f, 450f)))
                ),
                level = 30
            )
            else -> loadLevel(1) // Fallback to level 1
        }
    }

    private fun createLevel(
        playerPos: Offset,
        targetPos: Offset,
        obstacles: List<Obstacle>,
        level: Int
    ): GameState {
        return GameState(
            playerPos = playerPos,
            targetPos = targetPos,
            obstacles = obstacles,
            currentLevel = level,
            moveCharges = 3
        )
    }
}
