package com.example.gra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gra.audio.SoundManager
import com.example.gra.ui.*
import com.example.gra.ui.theme.GraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize sound system
        SoundManager.initialize(this)

        setContent {
            GraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val gameViewModel: GameViewModel = viewModel()

                    NavHost(navController = navController, startDestination = "menu") {
                        composable("menu") {
                            MainMenu(
                                onStartGame = { navController.navigate("game") },
                                onSelectLevel = { navController.navigate("levels") }
                            )
                        }
                        composable("levels") {
                            LevelSelect(onLevelSelected = { level ->
                                gameViewModel.loadLevel(level)
                                navController.navigate("game")
                            })
                        }
                        composable("game") {
                            GameScreen(gameViewModel)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundManager.release()
    }
}
