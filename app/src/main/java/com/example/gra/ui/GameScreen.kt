package com.example.gra.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gra.model.GameState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(viewModel: GameViewModel) {
    val state by viewModel.gameState.collectAsState()
    var formula by remember { mutableStateOf("") }
    var isMoveMode by remember { mutableStateOf(false) }
    var deltaX by remember { mutableStateOf("100") }
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var zoomScale by remember { mutableStateOf(1f) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        // Top HUD
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 4.dp,
            color = Color(0xFF1E1E1E)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Level ${state.currentLevel}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00E5FF)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        " × ${state.moveCharges}",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }

        // Game Canvas
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        panOffset += pan
                        zoomScale = (zoomScale * zoom).coerceIn(0.5f, 3f)
                    }
                }
        ) {
            GameCanvas(state, panOffset, zoomScale)
        }

        // Formula Display
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp,
            color = Color(0xFF2A2A2A)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    if (isMoveMode) "Move Mode: y =" else "Shoot Mode: y =",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    formula.ifEmpty { "..." },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isMoveMode) Color(0xFF4CAF50) else Color(0xFFFF9800),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        // Control Panel
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 8.dp,
            color = Color(0xFF1E1E1E)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Mode Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (isMoveMode) Icons.Default.Place else Icons.Default.Send,
                            contentDescription = null,
                            tint = if (isMoveMode) Color(0xFF4CAF50) else Color(0xFFFF9800)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (isMoveMode) "MOVE" else "SHOOT",
                            fontWeight = FontWeight.Bold,
                            color = if (isMoveMode) Color(0xFF4CAF50) else Color(0xFFFF9800)
                        )
                    }
                    Switch(
                        checked = isMoveMode,
                        onCheckedChange = { isMoveMode = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF4CAF50),
                            uncheckedThumbColor = Color(0xFFFF9800)
                        )
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Formula Buttons
                FormulaKeypad(
                    formula = formula,
                    onFormulaChange = { formula = it }
                )

                Spacer(Modifier.height(12.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isMoveMode) {
                        // Direction buttons for shooting
                        Button(
                            onClick = {
                                viewModel.onFire(formula, -1)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2)
                            )
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("FIRE ←")
                        }
                        Button(
                            onClick = {
                                viewModel.onFire(formula, 1)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2)
                            )
                        ) {
                            Text("FIRE →")
                            Spacer(Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null)
                        }
                    } else {
                        // Move button
                        TextField(
                            value = deltaX,
                            onValueChange = { deltaX = it },
                            modifier = Modifier.width(100.dp),
                            label = { Text("Δx") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF2A2A2A),
                                unfocusedContainerColor = Color(0xFF2A2A2A)
                            )
                        )
                        Button(
                            onClick = {
                                viewModel.onMove(formula, deltaX.toFloatOrNull() ?: 0f)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            )
                        ) {
                            Text("GO", fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormulaKeypad(formula: String, onFormulaChange: (String) -> Unit) {
    val buttons = listOf(
        listOf("7", "8", "9", "/", "x"),
        listOf("4", "5", "6", "*", "^"),
        listOf("1", "2", "3", "-", "sqrt"),
        listOf("0", ".", "(", ")", "+"),
        listOf("sin", "cos", "tan", "⌫", "C")
    )

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { symbol ->
                    Button(
                        onClick = {
                            when (symbol) {
                                "C" -> onFormulaChange("")
                                "⌫" -> if (formula.isNotEmpty()) {
                                    onFormulaChange(formula.dropLast(1))
                                }
                                "sqrt" -> onFormulaChange(formula + "sqrt(")
                                "sin" -> onFormulaChange(formula + "sin(")
                                "cos" -> onFormulaChange(formula + "cos(")
                                "tan" -> onFormulaChange(formula + "tan(")
                                else -> onFormulaChange(formula + symbol)
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (symbol) {
                                "C", "⌫" -> Color(0xFFD32F2F)
                                "x" -> Color(0xFF00E5FF)
                                in listOf("/", "*", "-", "+", "^") -> Color(0xFFFF9800)
                                in listOf("sin", "cos", "tan", "sqrt") -> Color(0xFF9C27B0)
                                else -> Color(0xFF424242)
                            }
                        ),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text(
                            symbol,
                            fontSize = if (symbol.length > 1) 12.sp else 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameCanvas(state: GameState, panOffset: Offset, zoomScale: Float) {
    Canvas(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0A0A))) {
        val centerOffset = Offset(size.width / 2, size.height / 2) + panOffset

        // Draw Grid
        val gridSize = 50f * zoomScale
        val startX = ((0 - centerOffset.x) / gridSize).toInt()
        val endX = ((size.width - centerOffset.x) / gridSize).toInt()
        val startY = ((0 - centerOffset.y) / gridSize).toInt()
        val endY = ((size.height - centerOffset.y) / gridSize).toInt()

        for (x in startX..endX) {
            val xPos = centerOffset.x + x * gridSize
            drawLine(
                Color(0xFF1A1A1A),
                Offset(xPos, 0f),
                Offset(xPos, size.height),
                strokeWidth = if (x % 5 == 0) 2f else 1f
            )
        }
        for (y in startY..endY) {
            val yPos = centerOffset.y + y * gridSize
            drawLine(
                Color(0xFF1A1A1A),
                Offset(0f, yPos),
                Offset(size.width, yPos),
                strokeWidth = if (y % 5 == 0) 2f else 1f
            )
        }

        // Transform positions for zoom/pan
        fun transformPoint(point: Offset): Offset {
            return Offset(
                centerOffset.x + point.x * zoomScale,
                centerOffset.y + point.y * zoomScale
            )
        }

        val playerScreenPos = transformPoint(state.playerPos)
        val targetScreenPos = transformPoint(state.targetPos)

        // Local Axes for Player
        val axisLength = 100f * zoomScale
        drawLine(
            Color(0xFF616161),
            Offset(playerScreenPos.x - axisLength, playerScreenPos.y),
            Offset(playerScreenPos.x + axisLength, playerScreenPos.y),
            strokeWidth = 2f
        )
        drawLine(
            Color(0xFF616161),
            Offset(playerScreenPos.x, playerScreenPos.y - axisLength),
            Offset(playerScreenPos.x, playerScreenPos.y + axisLength),
            strokeWidth = 2f
        )

        // Draw Player - larger with label
        drawCircle(Color(0xFF00E5FF), 20f * zoomScale, playerScreenPos)
        drawCircle(Color(0xFF006064), 16f * zoomScale, playerScreenPos)
        drawCircle(Color(0xFF00E5FF), 8f * zoomScale, playerScreenPos)

        // Draw Target - distinct red with crosshair
        drawCircle(Color(0xFFFF1744), 25f * zoomScale, targetScreenPos)
        drawCircle(Color(0xFFB71C1C), 20f * zoomScale, targetScreenPos)
        // Crosshair
        val crosshairSize = 15f * zoomScale
        drawLine(
            Color.White,
            Offset(targetScreenPos.x - crosshairSize, targetScreenPos.y),
            Offset(targetScreenPos.x + crosshairSize, targetScreenPos.y),
            strokeWidth = 3f
        )
        drawLine(
            Color.White,
            Offset(targetScreenPos.x, targetScreenPos.y - crosshairSize),
            Offset(targetScreenPos.x, targetScreenPos.y + crosshairSize),
            strokeWidth = 3f
        )

        // Draw Obstacles
        state.obstacles.forEach { obstacle ->
            val topLeft = transformPoint(obstacle.rect.topLeft)
            val size = obstacle.rect.size * zoomScale

            if (obstacle.destroyed) {
                // Draw destroyed obstacles as debris
                drawRect(
                    Color(0xFF424242).copy(alpha = 0.3f),
                    topLeft,
                    size
                )
                drawRect(
                    Color(0xFF757575).copy(alpha = 0.5f),
                    topLeft,
                    size,
                    style = Stroke(width = 2f)
                )
            } else {
                // Draw solid obstacles
                drawRect(Color(0xFF616161), topLeft, size)
                drawRect(
                    Color(0xFF9E9E9E),
                    topLeft,
                    size,
                    style = Stroke(width = 3f)
                )
            }
        }

        // Draw Projectiles
        state.projectiles.forEach { projectile ->
            if (projectile.points.size > 1) {
                for (i in 0 until projectile.points.size - 1) {
                    val p1 = transformPoint(projectile.points[i])
                    val p2 = transformPoint(projectile.points[i + 1])
                    drawLine(
                        Color(0xFFFFEB3B),
                        p1,
                        p2,
                        strokeWidth = 4f * zoomScale
                    )
                }
            }
        }
    }
}
