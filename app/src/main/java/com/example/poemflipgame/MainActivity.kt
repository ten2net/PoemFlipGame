package com.example.poemflipgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.poemflipgame.data.StatsRepository
import com.example.poemflipgame.model.GameMode
import com.example.poemflipgame.ui.GameScreen
import com.example.poemflipgame.ui.MenuScreen
import com.example.poemflipgame.ui.StatsScreen
import com.example.poemflipgame.ui.theme.PoemFlipGameTheme
import com.example.poemflipgame.viewmodel.GameViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    private val gameViewModel: GameViewModel by viewModels()
    private lateinit var statsRepository: StatsRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        statsRepository = StatsRepository(this)
        
        setContent {
            PoemFlipGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PoemFlipApp(
                        gameViewModel = gameViewModel,
                        statsRepository = statsRepository
                    )
                }
            }
        }
    }
}

@Composable
fun PoemFlipApp(
    gameViewModel: GameViewModel,
    statsRepository: StatsRepository
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    
    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(
                onStartGame = { gameMode ->
                    navController.navigate("game/${gameMode.javaClass.simpleName}")
                },
                onViewStats = {
                    navController.navigate("stats")
                }
            )
        }
        
        composable("game/{mode}") { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode")
            val gameMode = when (mode) {
                "Easy" -> GameMode.Easy
                "Hard" -> GameMode.Hard
                else -> GameMode.Easy
            }
            
            GameScreen(
                gameMode = gameMode,
                onBackToMenu = {
                    gameViewModel.resetGame()
                    navController.popBackStack()
                },
                onGameEnd = { isSuccess, timeUsed ->
                    scope.launch {
                        if (isSuccess) {
                            statsRepository.recordGameSuccess(timeUsed.toLong())
                        } else {
                            statsRepository.recordGameFailure()
                        }
                    }
                },
                viewModel = gameViewModel
            )
        }
        
        composable("stats") {
            StatsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}