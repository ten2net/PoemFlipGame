package com.example.poemflipgame.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.poemflipgame.model.GameMode
import com.example.poemflipgame.model.WoodColors
import com.example.poemflipgame.ui.components.WoodCard
import com.example.poemflipgame.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameMode: GameMode,
    onBackToMenu: () -> Unit,
    onGameEnd: (Boolean, Int) -> Unit,
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    
    // 初始化游戏
    if (!gameState.isGameActive && !gameState.isGameOver) {
        viewModel.startNewGame(gameMode)
    }
    
    // 游戏结束回调
    if (gameState.isGameOver) {
        val timeUsed = 60 - gameState.timeRemaining
        onGameEnd(gameState.isSuccess, timeUsed)
    }
    
    val columns = when (gameMode) {
        GameMode.Easy -> 3
        GameMode.Hard -> 5
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "古诗翻牌",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToMenu) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = "返回",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WoodColors.woodDark
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A1A2E),
                            Color(0xFF16213E)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 计时器和翻牌计数
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TimerCard(
                        timeRemaining = gameState.timeRemaining,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    FlipCounterCard(
                        flippedCount = gameState.flippedCount,
                        maxFlips = 5,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 提示文字
                Text(
                    text = "翻出5个字，组成一句古诗！",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 卡片网格
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(gameState.cards) { card ->
                        WoodCard(
                            card = card,
                            onClick = { viewModel.flipCard(card.id) },
                            modifier = Modifier
                                .aspectRatio(0.75f)
                        )
                    }
                }
            }
            
            // 游戏结束对话框
            if (gameState.isGameOver) {
                GameResultDialog(
                    isSuccess = gameState.isSuccess,
                    poem = gameState.currentPoem,
                    onPlayAgain = { viewModel.startNewGame(gameMode) },
                    onBackToMenu = onBackToMenu
                )
            }
        }
    }
}

@Composable
private fun TimerCard(timeRemaining: Int, modifier: Modifier = Modifier) {
    val isLowTime = timeRemaining <= 10
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isLowTime) Color(0xFFE74C3C) else WoodColors.woodMedium
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "时间",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
            Text(
                text = "${timeRemaining}s",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun FlipCounterCard(flippedCount: Int, maxFlips: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = WoodColors.woodMedium
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "已翻牌",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
            Text(
                text = "$flippedCount/$maxFlips",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GameResultDialog(
    isSuccess: Boolean,
    poem: com.example.poemflipgame.model.Poem?,
    onPlayAgain: () -> Unit,
    onBackToMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(
                text = if (isSuccess) "🎉 恭喜成功！" else "😔 游戏结束",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSuccess) Color(0xFF27AE60) else Color(0xFFE74C3C)
            )
        },
        text = {
            Column {
                if (isSuccess && poem != null) {
                    Text(
                        text = "你成功翻出了古诗：",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "《${poem.title}》",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = WoodColors.woodDark
                    )
                    Text(
                        text = "${poem.dynasty} · ${poem.author}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = poem.content,
                        fontSize = 16.sp,
                        color = WoodColors.woodText
                    )
                } else {
                    Text(
                        text = "时间到！再试一次吧。",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onPlayAgain,
                colors = ButtonDefaults.buttonColors(
                    containerColor = WoodColors.woodMedium
                )
            ) {
                Text("再玩一次")
            }
        },
        dismissButton = {
            TextButton(onClick = onBackToMenu) {
                Text("返回菜单", color = Color.Gray)
            }
        }
    )
}