package com.example.gra.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gra.model.GameState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(viewModel: GameViewModel, onNavigateBack: () -> Unit = {}) {
    val state by viewModel.gameState.collectAsState()

    var formulaText by remember { mutableStateOf(TextFieldValue("")) }
    var isMoveMode by remember { mutableStateOf(false) }
    var deltaX by remember { mutableStateOf("5") }
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var zoomScale by remember { mutableFloatStateOf(1f) }

    var showDefeatDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current.density

    // Check for defeat (no move charges and not at target)
    LaunchedEffect(state.moveCharges, state.playerPos, state.targetPos) {
        if (state.moveCharges == 0 && !state.showVictoryDialog) {
            val distanceToTarget = kotlin.math.sqrt(
                (state.playerPos.x - state.targetPos.x) * (state.playerPos.x - state.targetPos.x) +
                (state.playerPos.y - state.targetPos.y) * (state.playerPos.y - state.targetPos.y)
            )
            if (distanceToTarget > 10f) { // Far from target
                showDefeatDialog = true
            }
        }
    }

    // Prevent system keyboard from appearing
    DisposableEffect(Unit) {
        focusManager.clearFocus()
        onDispose { }
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
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to menu",
                            tint = Color.White
                        )
                    }
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
                    IconButton(onClick = {
                        viewModel.loadLevel(state.currentLevel)
                        formulaText = TextFieldValue("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Restart level",
                            tint = Color.White
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

                // Formula Display with cursor
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

                        // Custom text display with cursor
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .pointerInput(formulaText.text) {
                                    detectTapGestures { offset ->
                                        // Measure text to find cursor position from tap
                                        val text = formulaText.text
                                        if (text.isNotEmpty()) {
                                            val charWidth = 12f // Approximate character width in sp=20
                                            val tapX = offset.x
                                            val estimatedPos = (tapX / (charWidth * density)).toInt()
                                                .coerceIn(0, text.length)

                                            formulaText = formulaText.copy(selection = TextRange(estimatedPos))
                                        }
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val displayText = formulaText.text.ifEmpty { "..." }
                            val cursorPos = formulaText.selection.start.coerceIn(0, formulaText.text.length)

                            if (formulaText.text.isEmpty()) {
                                Text(
                                    text = displayText,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray.copy(alpha = 0.5f)
                                )
                            } else {
                                val beforeCursor = formulaText.text.substring(0, cursorPos)
                                val afterCursor = formulaText.text.substring(cursorPos)

                                Text(
                                    text = beforeCursor,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMoveMode) Color(0xFF66BB6A) else Color(0xFFFFB300)
                                )

                                // Blinking cursor
                                var showCursor by remember { mutableStateOf(true) }
                                LaunchedEffect(Unit) {
                                    while (true) {
                                        delay(500.milliseconds)
                                        showCursor = !showCursor
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(24.dp)
                                        .background(
                                            if (showCursor) Color.White else Color.Transparent
                                        )
                                )

                                Text(
                                    text = afterCursor,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMoveMode) Color(0xFF66BB6A) else Color(0xFFFFB300)
                                )
                            }
                        }
                    }
                }

                // Formula Keypad (Scrollable)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FormulaKeypad(
                        formula = formulaText,
                        onFormulaChange = { formulaText = it }
                    )
                }

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
                                containerColor = Color(0xFF1565C0),
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 6.dp,
                                pressedElevation = 2.dp
                            )
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(4.dp))
                            Text("FIRE", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Button(
                            onClick = {
                                viewModel.onFire(formulaText.text, 1)
                            },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1565C0),
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 6.dp,
                                pressedElevation = 2.dp
                            )
                        ) {
                            Text("FIRE", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(Modifier.width(4.dp))
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White)
                        }
                    }
                } else {
                    // Move button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = deltaX,
                            onValueChange = { deltaX = it },
                            modifier = Modifier.weight(0.4f),
                            label = { Text("Δx", fontWeight = FontWeight.Bold) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF2A2A2A),
                                unfocusedContainerColor = Color(0xFF2A2A2A),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF66BB6A),
                                unfocusedBorderColor = Color(0xFF424242)
                            ),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                viewModel.onMove(formulaText.text, deltaX.toFloatOrNull() ?: 0f)
                            },
                            modifier = Modifier.weight(0.6f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2E7D32),
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 6.dp,
                                pressedElevation = 2.dp
                            )
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.White)
                            Spacer(Modifier.width(4.dp))
                            Text("GO", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Victory Dialog
    if (state.showVictoryDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    "Level Complete!",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00E5FF)
                )
            },
            text = {
                Column {
                    Text("Great work! You hit the target!", color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text("Shots fired: ${state.shotsFired}", fontSize = 14.sp, color = Color.White)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (state.currentLevel >= 30) {
                            onNavigateBack()
                        } else {
                            viewModel.loadLevel(state.currentLevel + 1)
                            formulaText = TextFieldValue("")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00E5FF),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        if (state.currentLevel >= 30) "Back to Menu" else "Next Level",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onNavigateBack) {
                    Text("Menu", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color(0xFF1E1E1E)
        )
    }

    // Defeat Dialog
    if (showDefeatDialog) {
        AlertDialog(
            onDismissRequest = { showDefeatDialog = false },
            title = {
                Text(
                    "Out of Moves!",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF5722)
                )
            },
            text = {
                Text(
                    "You've run out of movement charges and are too far from the target.\n\nTry a different strategy!",
                    color = Color.White
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDefeatDialog = false
                        viewModel.loadLevel(state.currentLevel)
                        formulaText = TextFieldValue("")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF5722),
                        contentColor = Color.White
                    )
                ) {
                    Text("Retry", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = onNavigateBack) {
                    Text("Menu", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color(0xFF1E1E1E)
        )
    }
}

@Composable
fun FormulaKeypad(formula: TextFieldValue, onFormulaChange: (TextFieldValue) -> Unit) {
    val buttons = listOf(
        listOf("7", "8", "9", "÷", "x"),
        listOf("4", "5", "6", "×", "^"),
        listOf("1", "2", "3", "-", "("),
        listOf("0", ".", "+", ")", "√"),
        listOf("sin", "cos", "tan", "abs", "π"),
        listOf("log", "ln", "e", "◄", "►"),
        listOf("C", "", "", "", "⌫")
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
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
                                        // Smart delete: remove entire function names
                                        val mathFunctions = listOf("sin(", "cos(", "tan(", "log(", "ln(", "abs(", "sqrt(")
                                        var charsToDelete = 1

                                        // Check if cursor is right after a math function
                                        for (func in mathFunctions) {
                                            if (cursorPos >= func.length &&
                                                currentText.substring(cursorPos - func.length, cursorPos) == func) {
                                                charsToDelete = func.length
                                                break
                                            }
                                        }

                                        val newText = currentText.substring(0, cursorPos - charsToDelete) +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(
                                            TextFieldValue(
                                                newText,
                                                TextRange(cursorPos - charsToDelete)
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
                                    "sin", "cos", "tan", "log", "abs", "ln" -> {
                                        val newText = currentText.substring(0, cursorPos) + symbol + "(" +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(TextFieldValue(newText, TextRange(cursorPos + symbol.length + 1)))
                                    }
                                    "π" -> {
                                        val newText = currentText.substring(0, cursorPos) + "π" +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(TextFieldValue(newText, TextRange(cursorPos + 1)))
                                    }
                                    "e" -> {
                                        val newText = currentText.substring(0, cursorPos) + "e" +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(TextFieldValue(newText, TextRange(cursorPos + 1)))
                                    }
                                    else -> {
                                        val newText = currentText.substring(0, cursorPos) + symbol +
                                                     currentText.substring(cursorPos)
                                        onFormulaChange(TextFieldValue(newText, TextRange(cursorPos + symbol.length)))
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when (symbol) {
                                    "C" -> Color(0xFFD32F2F)
                                    "⌫" -> Color(0xFFE64A19)
                                    "x" -> Color(0xFF00E5FF)
                                    "π", "e" -> Color(0xFF9C27B0)
                                    in listOf("÷", "×", "-", "+", "^") -> Color(0xFFFF9800)
                                    in listOf("sin", "cos", "tan", "log", "abs", "√", "ln") -> Color(0xFF7B1FA2)
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
                                fontSize = if (symbol.length > 2) 11.sp else 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
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

        // Calculate visible grid range - IMPROVED ALGORITHM
        // Transform screen corners to world coordinates to get exact range
        val screenTopLeft = Offset(0f, 0f)
        val screenBottomRight = Offset(size.width, size.height)

        // Convert screen coordinates to world coordinates
        val worldLeft = state.playerPos.x + (screenTopLeft.x - centerOffset.x) / gridSize
        val worldRight = state.playerPos.x + (screenBottomRight.x - centerOffset.x) / gridSize
        val worldTop = state.playerPos.y + (centerOffset.y - screenTopLeft.y) / gridSize
        val worldBottom = state.playerPos.y + (centerOffset.y - screenBottomRight.y) / gridSize

        // Add generous margin to prevent line disappearance
        val startX = kotlin.math.floor(worldLeft).toInt() - 10
        val endX = kotlin.math.ceil(worldRight).toInt() + 10
        val startY = kotlin.math.floor(worldBottom).toInt() - 10
        val endY = kotlin.math.ceil(worldTop).toInt() + 10

        // Draw Grid - vertical lines (X-axis lines)
        for (x in startX..endX) {
            val screenX = centerOffset.x + (x - state.playerPos.x) * gridSize
            // Draw even if slightly outside screen to prevent gaps
            if (screenX >= -gridSize && screenX <= size.width + gridSize) {
                drawLine(
                    color = if (x == 0) Color(0xFF757575) else Color(0xFF2A2A2A),
                    start = Offset(screenX, 0f),
                    end = Offset(screenX, size.height),
                    strokeWidth = if (x % 5 == 0) 3f else 2f
                )
            }
        }

        // Draw Grid - horizontal lines (Y-axis lines)
        for (y in startY..endY) {
            val screenY = centerOffset.y - (y - state.playerPos.y) * gridSize
            // Draw even if slightly outside screen to prevent gaps
            if (screenY >= -gridSize && screenY <= size.height + gridSize) {
                drawLine(
                    color = if (y == 0) Color(0xFF757575) else Color(0xFF2A2A2A),
                    start = Offset(0f, screenY),
                    end = Offset(size.width, screenY),
                    strokeWidth = if (y % 5 == 0) 3f else 2f
                )
            }
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

        // Draw Obstacles (circles) with holes
        state.obstacles.forEach { obstacle ->
            val obstacleCenter = transformPoint(obstacle.center)
            val obstacleRadius = obstacle.radius * gridSize

            if (obstacle.destroyed) {
                // Destroyed - very transparent outline
                drawCircle(
                    Color(0xFF424242).copy(alpha = 0.15f),
                    obstacleRadius,
                    obstacleCenter,
                    style = Stroke(width = 2f)
                )
            } else {
                // Calculate health percentage based on holes
                val healthPercent = 1f - (obstacle.holes.size / 20f).coerceAtMost(1f)

                // Color based on health
                val obstacleColor = when {
                    healthPercent > 0.66f -> Color(0xFF616161) // Full health - dark gray
                    healthPercent > 0.33f -> Color(0xFF9E9E9E) // Medium - gray
                    else -> Color(0xFFBDBDBD) // Low - light gray
                }

                // Draw main obstacle circle
                drawCircle(
                    obstacleColor,
                    obstacleRadius,
                    obstacleCenter
                )

                // Draw outline
                drawCircle(
                    Color(0xFFFFFFFF).copy(alpha = healthPercent * 0.5f),
                    obstacleRadius,
                    obstacleCenter,
                    style = Stroke(width = 3f)
                )

                // Draw holes (cut-outs)
                obstacle.holes.forEach { hole ->
                    val holeCenter = transformPoint(hole.center)
                    val holeRadius = hole.radius * gridSize

                    // Draw hole as dark circle
                    drawCircle(
                        Color(0xFF0A0A0A), // Background color
                        holeRadius,
                        holeCenter
                    )

                    // Draw hole outline
                    drawCircle(
                        Color(0xFF757575).copy(alpha = 0.6f),
                        holeRadius,
                        holeCenter,
                        style = Stroke(width = 1.5f)
                    )
                }
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

        // Draw Explosions
        state.explosions.forEach { explosion ->
            if (explosion.isActive) {
                val explosionPos = transformPoint(explosion.position)
                val maxRadius = 2f * gridSize
                val currentRadius = maxRadius * explosion.progress
                val alpha = (1f - explosion.progress).coerceIn(0f, 1f)

                // Outer ring - orange
                drawCircle(
                    Color(0xFFFF9800).copy(alpha = alpha * 0.6f),
                    currentRadius,
                    explosionPos
                )

                // Inner ring - yellow
                drawCircle(
                    Color(0xFFFFEB3B).copy(alpha = alpha * 0.8f),
                    currentRadius * 0.7f,
                    explosionPos
                )

                // Core - white
                drawCircle(
                    Color.White.copy(alpha = alpha),
                    currentRadius * 0.3f,
                    explosionPos
                )
            }
        }
    }
}
