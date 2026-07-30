package com.example.gra.engine

import androidx.compose.ui.geometry.Offset
import com.example.gra.model.GameState
import com.example.gra.model.Obstacle
import com.example.gra.model.FieldBounds

object LevelManager {
    fun loadLevel(level: Int): GameState {
        return when (level) {
            // === TUTORIAL: BASICS (1-5) ===

            // Level 1: Introduction - straight horizontal line (y=0)
            1 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(12f, 0f),
                obstacles = emptyList(),
                level = 1
            )

            // Level 2: Simple linear function (y=x) - 45 degrees up
            2 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(10f, 10f),
                obstacles = emptyList(),
                level = 2
            )

            // Level 3: Negative slope (y=-x) - 45 degrees down
            3 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(12f, -12f),
                obstacles = emptyList(),
                level = 3
            )

            // Level 4: First obstacle - need parabola to go over (y=0.5*x-0.02*x^2)
            4 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(16f, 0f),
                obstacles = listOf(
                    Obstacle(center = Offset(8f, 0f), radius = 2.5f)
                ),
                level = 4
            )

            // Level 5: Going under obstacle - inverted parabola (y=-0.5*x-0.02*x^2)
            5 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(18f, -4f),
                obstacles = listOf(
                    Obstacle(center = Offset(9f, 3f), radius = 3f)
                ),
                level = 5
            )

            // === INTERMEDIATE: PARABOLAS & COEFFICIENTS (6-10) ===

            // Level 6: Steep parabola - teaching coefficient control
            6 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(15f, 8f),
                obstacles = listOf(
                    Obstacle(center = Offset(7f, 2f), radius = 2f)
                ),
                level = 6
            )

            // Level 7: Multiple obstacles in line - slalom (y=sin(x))
            7 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(20f, 0f),
                obstacles = listOf(
                    Obstacle(center = Offset(8f, 0f), radius = 2f),
                    Obstacle(center = Offset(14f, 0f), radius = 2f)
                ),
                level = 7
            )

            // Level 8: Target above - high arc needed
            8 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(20f, 12f),
                obstacles = listOf(
                    Obstacle(center = Offset(10f, 4f), radius = 3f)
                ),
                level = 8
            )

            // Level 9: Target below with obstacles above - inverted arc
            9 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(22f, -10f),
                obstacles = listOf(
                    Obstacle(center = Offset(11f, 2f), radius = 3f),
                    Obstacle(center = Offset(17f, 1f), radius = 2.5f)
                ),
                level = 9
            )

            // Level 10: Narrow gap - precision required
            10 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(24f, 0f),
                obstacles = listOf(
                    Obstacle(center = Offset(12f, 3.5f), radius = 2.5f),
                    Obstacle(center = Offset(12f, -3.5f), radius = 2.5f)
                ),
                level = 10
            )

            // === ADVANCED: TRIGONOMETRY (11-15) ===

            // Level 11: Introduction to sine waves (y=3*sin(x))
            11 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(15f, -2f),
                obstacles = listOf(
                    Obstacle(center = Offset(8f, 3f), radius = 2f)
                ),
                level = 11
            )

            // Level 12: Left side shooting - negative direction
            12 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-18f, 6f),
                obstacles = listOf(
                    Obstacle(center = Offset(-9f, 2f), radius = 3f)
                ),
                level = 12
            )

            // Level 13: Sine wave through obstacles
            13 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(25f, 0f),
                obstacles = listOf(
                    Obstacle(center = Offset(8f, 0f), radius = 2f),
                    Obstacle(center = Offset(16f, 0f), radius = 2f)
                ),
                level = 13
            )

            // Level 14: Cosine introduction - phase shift
            14 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(20f, 8f),
                obstacles = listOf(
                    Obstacle(center = Offset(10f, 0f), radius = 3f)
                ),
                level = 14
            )

            // Level 15: Complex wave pattern (y=2*sin(x)+x/3)
            15 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(28f, 6f),
                obstacles = listOf(
                    Obstacle(center = Offset(10f, 2f), radius = 2.5f),
                    Obstacle(center = Offset(18f, 4f), radius = 2.5f)
                ),
                level = 15
            )

            // === EXPERT: COMBINATIONS (16-20) ===

            // Level 16: Exponential intro (y=e^(x/10))
            16 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(25f, 10f),
                obstacles = listOf(
                    Obstacle(center = Offset(12f, 3f), radius = 3f),
                    Obstacle(center = Offset(19f, 6f), radius = 3f)
                ),
                level = 16
            )

            // Level 17: Logarithmic curve (y=5*log(x+1))
            17 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-30f, 12f),
                obstacles = listOf(
                    Obstacle(center = Offset(-15f, 6f), radius = 4f),
                    Obstacle(center = Offset(-22f, 9f), radius = 3f)
                ),
                level = 17
            )

            // Level 18: Square root curve (y=sqrt(abs(x))*2)
            18 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(35f, 10f),
                obstacles = listOf(
                    Obstacle(center = Offset(15f, 6f), radius = 3.5f),
                    Obstacle(center = Offset(25f, 8f), radius = 3.5f)
                ),
                level = 18
            )

            // Level 19: Combined functions (y=sin(x)*x/2)
            19 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(30f, -8f),
                obstacles = listOf(
                    Obstacle(center = Offset(12f, -2f), radius = 3f),
                    Obstacle(center = Offset(20f, -5f), radius = 3f)
                ),
                level = 19
            )

            // Level 20: The Wall - multiple obstacles to destroy
            20 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(40f, 0f),
                obstacles = listOf(
                    Obstacle(center = Offset(15f, 0f), radius = 4f),
                    Obstacle(center = Offset(24f, 0f), radius = 4f),
                    Obstacle(center = Offset(33f, 0f), radius = 4f)
                ),
                level = 20
            )

            // === MASTER: CREATIVE SOLUTIONS (21-25) ===

            // Level 21: Tangent function intro (y=tan(x/5))
            21 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-35f, -15f),
                obstacles = listOf(
                    Obstacle(center = Offset(-18f, -5f), radius = 4f),
                    Obstacle(center = Offset(-27f, -10f), radius = 3.5f)
                ),
                level = 21
            )

            // Level 22: Absolute value (y=abs(x-15)-5)
            22 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(30f, -5f),
                obstacles = listOf(
                    Obstacle(center = Offset(15f, 5f), radius = 5f)
                ),
                level = 22
            )

            // Level 23: Pi usage (y=3*sin(pi*x/10))
            23 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(40f, 0f),
                obstacles = listOf(
                    Obstacle(center = Offset(12f, 0f), radius = 2.5f),
                    Obstacle(center = Offset(20f, 0f), radius = 2.5f),
                    Obstacle(center = Offset(28f, 0f), radius = 2.5f),
                    Obstacle(center = Offset(36f, 0f), radius = 2.5f)
                ),
                level = 23
            )

            // Level 24: Polynomial (y=0.01*x^3-0.3*x^2+x)
            24 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-38f, 10f),
                obstacles = listOf(
                    Obstacle(center = Offset(-20f, 5f), radius = 4f),
                    Obstacle(center = Offset(-30f, 8f), radius = 3.5f)
                ),
                level = 24
            )

            // Level 25: Damped oscillation (y=e^(-x/10)*sin(x)*5)
            25 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(45f, -2f),
                obstacles = listOf(
                    Obstacle(center = Offset(15f, 3f), radius = 3f),
                    Obstacle(center = Offset(25f, 1f), radius = 3f),
                    Obstacle(center = Offset(35f, 0f), radius = 2.5f)
                ),
                level = 25
            )

            // === GRANDMASTER: ULTIMATE CHALLENGES (26-30) ===

            // Level 26: The Fortress - multiple layers
            26 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(50f, 0f),
                obstacles = listOf(
                    Obstacle(center = Offset(15f, 0f), radius = 5f),
                    Obstacle(center = Offset(25f, 5f), radius = 4f),
                    Obstacle(center = Offset(25f, -5f), radius = 4f),
                    Obstacle(center = Offset(38f, 0f), radius = 5f)
                ),
                level = 26
            )

            // Level 27: Spiral approach (y=x*sin(x/3))
            27 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-50f, -20f),
                obstacles = listOf(
                    Obstacle(center = Offset(-15f, -5f), radius = 4f),
                    Obstacle(center = Offset(-28f, -10f), radius = 4.5f),
                    Obstacle(center = Offset(-40f, -15f), radius = 4f)
                ),
                level = 27
            )

            // Level 28: Double wave (y=sin(x)+cos(x*2))
            28 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(55f, 5f),
                obstacles = listOf(
                    Obstacle(center = Offset(15f, 2f), radius = 4f),
                    Obstacle(center = Offset(28f, 0f), radius = 4.5f),
                    Obstacle(center = Offset(40f, 3f), radius = 4f)
                ),
                level = 28
            )

            // Level 29: The Gauntlet - precision slalom
            29 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(50f, 0f),
                obstacles = listOf(
                    Obstacle(center = Offset(10f, 4f), radius = 3f),
                    Obstacle(center = Offset(15f, -4f), radius = 3f),
                    Obstacle(center = Offset(22f, 4f), radius = 3f),
                    Obstacle(center = Offset(29f, -4f), radius = 3f),
                    Obstacle(center = Offset(38f, 4f), radius = 3f),
                    Obstacle(center = Offset(45f, -4f), radius = 3f)
                ),
                level = 29
            )

            // Level 30: FINAL BOSS - The Mathematical Masterpiece
            30 -> createLevel(
                playerPos = Offset(0f, 0f),
                targetPos = Offset(-60f, -25f),
                obstacles = listOf(
                    Obstacle(center = Offset(-10f, -3f), radius = 4f),
                    Obstacle(center = Offset(-18f, -6f), radius = 4.5f),
                    Obstacle(center = Offset(-25f, -10f), radius = 5f),
                    Obstacle(center = Offset(-32f, -14f), radius = 5f),
                    Obstacle(center = Offset(-40f, -17f), radius = 4.5f),
                    Obstacle(center = Offset(-50f, -21f), radius = 4f)
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
                   obstacles.flatMap { listOf(it.center.x - it.radius, it.center.x + it.radius) }
        val allY = listOf(playerPos.y, targetPos.y) +
                   obstacles.flatMap { listOf(it.center.y - it.radius, it.center.y + it.radius) }

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
