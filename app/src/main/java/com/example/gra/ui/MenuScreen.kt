package com.example.gra.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenu(onStartGame: () -> Unit, onSelectLevel: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D47A1),
                        Color(0xFF1A237E),
                        Color(0xFF000051)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated Title
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(animationSpec = tween(800))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "GRAPHWAR II",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF00E5FF),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        "Mathematical Artillery",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF80DEEA)
                    )
                }
            }

            Spacer(Modifier.height(80.dp))

            // Animated Buttons
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 300)) +
                        slideInVertically(
                            animationSpec = tween(800, delayMillis = 300),
                            initialOffsetY = { it / 2 }
                        )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    MenuButton(
                        text = "START GAME",
                        icon = Icons.Default.PlayArrow,
                        onClick = onStartGame,
                        color = Color(0xFF00E5FF)
                    )

                    Spacer(Modifier.height(20.dp))

                    MenuButton(
                        text = "LEVEL SELECT",
                        icon = Icons.Default.List,
                        onClick = onSelectLevel,
                        color = Color(0xFF1DE9B6)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    color: Color
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .width(280.dp)
            .height(70.dp)
            .scale(scale),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp
        )
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LevelSelect(onLevelSelected: (Int) -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A237E),
                        Color(0xFF000051)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(600)) +
                        slideInVertically(animationSpec = tween(500))
            ) {
                Text(
                    "SELECT LEVEL",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00E5FF),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(800, delayMillis = 200)) +
                        expandVertically(animationSpec = tween(600, delayMillis = 200))
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(30) { index ->
                        LevelButton(
                            level = index + 1,
                            onClick = { onLevelSelected(index + 1) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LevelButton(level: Int, onClick: () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    val color = when {
        level <= 10 -> Color(0xFF4CAF50) // Easy - Green
        level <= 20 -> Color(0xFFFF9800) // Medium - Orange
        else -> Color(0xFFF44336) // Hard - Red
    }

    Button(
        onClick = {
            isHovered = true
            onClick()
        },
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            "$level",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
