package com.example.gra.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gra.model.GameState
import kotlin.math.PI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(viewModel: GameViewModel) {
    val state by viewModel.gameState.collectAsState()

    var formulaText by remember { mutableStateOf(TextFieldValue("")) }
    var isMoveMode by remember { mutableStateOf(false) }
    var deltaX by remember { mutableStateOf("5") }
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var zoomScale by remember { mutableFloatStateOf(1f) }

    // Update camera offset in ViewModel
    LaunchedEffect(panOffset) {
        viewModel.updateCameraOffset(panOffset)
    }

    Row(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0A0A))) {
        // Game Canvas (left side, larger)
        Box(
            modifier = Modifier
                .weight(0.65f)
                .fillMaxHeight()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        panOffset += pan
                        zoomScale = (zoomScale * zoom).coerceIn(0.3f, 5f)
                    }
                }
        ) {
            GameCanvas(state, panOffset, zoomScale)

            // Top HUD overlay
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                tonalElevation = 6.dp,
                color = Color(0xDD1E1E1E),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Level ${state.currentLevel}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E5FF)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            " × ${state.moveCharges}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }

            // Center on player button
            FloatingActionButton(
                onClick = {
                    panOffset = Offset.Zero
                    zoomScale = 1f
                    viewModel.centerOnPlayer()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Color(0xFF00E5FF)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Center on player",
                    tint = Color.Black
                )
            }
        }

        // Control Panel (right side)
        Surface(
            modifier = Modifier
                .weight(0.35f)
                .fillMaxHeight(),
            tonalElevation = 8.dp,
            color = Color(0xFF1A1A1A)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Mode Switch
                Surface(
                    color = if (isMoveMode) Color(0xFF2E7D32) else Color(0xFFE65100),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isMoveMode) Icons.Default.Place else Icons.AutoMirrored.Filled.Send,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                if (isMoveMode) "MOVE" else "SHOOT",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            )
                        }
                        Switch(
                            checked = isMoveMode,
                            onCheckedChange = { isMoveMode = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                uncheckedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF66BB6A),
                                uncheckedTrackColor = Color(0xFFFF9800)
                            )
                        )
                    }
                }

                // Formula Display
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 2.dp,
                    color = Color(0xFF2A2A2A),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "y =",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        BasicTextField(
                            value = formulaText,
                            onValueChange = { formulaText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isMoveMode) Color(0xFF66BB6A) else Color(0xFFFFB300)
                            ),
                            decorationBox = { innerTextField ->
                                if (formulaText.text.isEmpty()) {
                                    Text(
                                        "...",
                                        fontSize = 20.sp,
                                        color = Color.Gray
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                // Formula Keypad
                FormulaKeypad(
                    formula = formulaText,
                    onFormulaChange = { formulaText = it }
                )

                Spacer(Modifier.height(4.dp))

                // Action Buttons
                if (!isMoveMode) {
                    // Direction buttons for shooting
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.onFire(formulaText.text, -1)
                            },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1565C0)
                            )
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("FIRE", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = {
                                viewModel.onFire(formulaText.text, 1)
                            },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1565C0)
                            )
                        ) {
                            Text("FIRE", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(4.dp))
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                } else {
                    // Move button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            value = deltaX,
                            onValueChange = { deltaX = it },
                            modifier = Modifier.weight(0.4f),
                            label = { Text("Δx", fontWeight = FontWeight.Bold) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF2A2A2A),
                                unfocusedContainerColor = Color(0xFF2A2A2A),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                        )
                        Button(
                            onClick = {
                                viewModel.onMove(formulaText.text, deltaX.toFloatOrNull() ?: 0f)
                            },
                            modifier = Modifier.weight(0.6f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2E7D32)
                            )
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("GO", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormulaKeypad(formula: TextFieldValue, onFormulaChange: (TextFieldValue) -> Unit) {
    val buttons = listOf(
        listOf("7", "8", "9", "÷", "x"),
        listOf("4", "5", "6", "×", "^"),
        listOf("1", "2", "3", "-", "√"),
        listOf("0", ".", "(", ")", "+"),
        listOf("sin", "cos", "abs", "π", "←"),
        listOf("tan", "log", "◄", "►", "⌫"),
        listOf("C", "", "", "", "")
    )

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                row.forEach { symbol ->
                    if (symbol.isNotEmpty()) {
                        Button(
                            onClick = {
                                val currentText = formula.text
                                val cursorPos = formula.selection.start

                                when (symbol) {
                                    "C" -> onFormulaChange(TextFieldValue(""))
                                    "⌫" -> if (cursorPos > 0) {
                                        val newText = currentText.substring(0, cursorPos - 1) +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(
                                            TextFieldValue(
                                                newText,
                                                TextRange(cursorPos - 1)
                                            )
                                        )
                                    }
                                    "◄" -> if (cursorPos > 0) {
                                        onFormulaChange(formula.copy(selection = TextRange(cursorPos - 1)))
                                    }
                                    "►" -> if (cursorPos < currentText.length) {
                                        onFormulaChange(formula.copy(selection = TextRange(cursorPos + 1)))
                                    }
                                    "÷" -> {
                                        val newText = currentText.substring(0, cursorPos) + "/" +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(TextFieldValue(newText, TextRange(cursorPos + 1)))
                                    }
                                    "×" -> {
                                        val newText = currentText.substring(0, cursorPos) + "*" +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(TextFieldValue(newText, TextRange(cursorPos + 1)))
                                    }
                                    "√" -> {
                                        val newText = currentText.substring(0, cursorPos) + "sqrt(" +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(TextFieldValue(newText, TextRange(cursorPos + 5)))
                                    }
                                    "sin", "cos", "tan", "log", "abs" -> {
                                        val newText = currentText.substring(0, cursorPos) + symbol + "(" +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(TextFieldValue(newText, TextRange(cursorPos + symbol.length + 1)))
                                    }
                                    "π" -> {
                                        val piValue = PI.toString()
                                        val newText = currentText.substring(0, cursorPos) + piValue +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(TextFieldValue(newText, TextRange(cursorPos + piValue.length)))
                                    }
                                    else -> {
                                        val newText = currentText.substring(0, cursorPos) + symbol +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(TextFieldValue(newText, TextRange(cursorPos + symbol.length)))
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f).height(44.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when (symbol) {
                                    "C" -> Color(0xFFD32F2F)
                                    "⌫" -> Color(0xFFE64A19)
                                    "x" -> Color(0xFF00E5FF)
                                    "π" -> Color(0xFF9C27B0)
                                    in listOf("÷", "×", "-", "+", "^") -> Color(0xFFFF9800)
                                    in listOf("sin", "cos", "tan", "log", "abs", "√") -> Color(0xFF7B1FA2)
                                    in listOf("◄", "►") -> Color(0xFF455A64)
                                    else -> Color(0xFF424242)
                                }
                            ),
                            contentPadding = PaddingValues(2.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            Text(
                                symbol,
                                fontSize = if (symbol.length > 2) 10.sp else 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Spacer(Modifier.weight(1f))
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

        // Grid size in pixels (1 unit = gridSize pixels)
        val gridSize = 40f * zoomScale

        // Transform from world coordinates to screen coordinates
        fun transformPoint(worldPos: Offset): Offset {
            return Offset(
                centerOffset.x + (worldPos.x - state.playerPos.x) * gridSize,
                centerOffset.y - (worldPos.y - state.playerPos.y) * gridSize
            )
        }

        // Calculate visible grid range
        val startX = ((-centerOffset.x) / gridSize).toInt() - 2 + state.playerPos.x.toInt()
        val endX = ((size.width - centerOffset.x) / gridSize).toInt() + 2 + state.playerPos.x.toInt()
        val startY = ((-centerOffset.y) / gridSize).toInt() - 2 + state.playerPos.y.toInt()
        val endY = ((size.height - centerOffset.y) / gridSize).toInt() + 2 + state.playerPos.y.toInt()

        // Draw Grid
        for (x in startX..endX) {
            val screenX = centerOffset.x + (x - state.playerPos.x) * gridSize
            drawLine(
                if (x == 0) Color(0xFF424242) else Color(0xFF1A1A1A),
                Offset(screenX, 0f),
                Offset(screenX, size.height),
                strokeWidth = if (x % 5 == 0) 2f else 1f
            )
        }
        for (y in startY..endY) {
            val screenY = centerOffset.y - (y - state.playerPos.y) * gridSize
            drawLine(
                if (y == 0) Color(0xFF424242) else Color(0xFF1A1A1A),
                Offset(0f, screenY),
                Offset(size.width, screenY),
                strokeWidth = if (y % 5 == 0) 2f else 1f
            )
        }

        // Draw field boundaries
        val bounds = state.fieldBounds
        val topLeft = transformPoint(Offset(bounds.minX, bounds.maxY))
        val bottomRight = transformPoint(Offset(bounds.maxX, bounds.minY))
        drawRect(
            Color(0xFFFF5722).copy(alpha = 0.3f),
            topLeft,
            androidx.compose.ui.geometry.Size(
                bottomRight.x - topLeft.x,
                bottomRight.y - topLeft.y
            ),
            style = Stroke(width = 3f)
        )

        val playerScreenPos = transformPoint(state.playerPos)

        // Local Axes for Player
        val axisLength = 5f * gridSize
        drawLine(
            Color(0xFF757575),
            Offset(playerScreenPos.x - axisLength, playerScreenPos.y),
            Offset(playerScreenPos.x + axisLength, playerScreenPos.y),
            strokeWidth = 2f
        )
        drawLine(
            Color(0xFF757575),
            Offset(playerScreenPos.x, playerScreenPos.y - axisLength),
            Offset(playerScreenPos.x, playerScreenPos.y + axisLength),
            strokeWidth = 2f
        )

        // Draw Obstacles
        state.obstacles.forEach { obstacle ->
            val topLeft = transformPoint(obstacle.rect.topLeft)
            val size = androidx.compose.ui.geometry.Size(
                obstacle.rect.width * gridSize,
                obstacle.rect.height * gridSize
            )

            if (obstacle.destroyed) {
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
                drawRect(Color(0xFF616161), topLeft, size)
                drawRect(
                    Color(0xFFBDBDBD),
                    topLeft,
                    size,
                    style = Stroke(width = 3f)
                )
            }
        }

        // Draw Target
        val targetScreenPos = transformPoint(state.targetPos)
        val targetRadius = 1.5f * gridSize
        drawCircle(Color(0xFFFF1744), targetRadius, targetScreenPos)
        drawCircle(Color(0xFFB71C1C), targetRadius * 0.7f, targetScreenPos)
        drawLine(
            Color.White,
            Offset(targetScreenPos.x - targetRadius, targetScreenPos.y),
            Offset(targetScreenPos.x + targetRadius, targetScreenPos.y),
            strokeWidth = 3f
        )
        drawLine(
            Color.White,
            Offset(targetScreenPos.x, targetScreenPos.y - targetRadius),
            Offset(targetScreenPos.x, targetScreenPos.y + targetRadius),
            strokeWidth = 3f
        )

        // Draw Player
        val playerRadius = 1f * gridSize
        drawCircle(Color(0xFF00E5FF), playerRadius, playerScreenPos)
        drawCircle(Color(0xFF006064), playerRadius * 0.7f, playerScreenPos)
        drawCircle(Color(0xFF00E5FF), playerRadius * 0.3f, playerScreenPos)

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
                        strokeWidth = 3f
                    )
                }
            }
        }
    }
}
