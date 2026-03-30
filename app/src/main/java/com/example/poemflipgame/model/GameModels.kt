package com.example.poemflipgame.model

data class PoemCard(
    val id: Int,
    val char: Char,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false,
    val isSelected: Boolean = false
)

sealed class GameMode {
    object Easy : GameMode() // 3x3
    object Hard : GameMode() // 5x5
}

data class GameState(
    val cards: List<PoemCard> = emptyList(),
    val selectedCards: List<Int> = emptyList(),
    val timeRemaining: Int = 60,
    val isGameActive: Boolean = false,
    val isGameOver: Boolean = false,
    val isSuccess: Boolean = false,
    val currentPoem: Poem? = null,
    val flippedCount: Int = 0,
    val gameMode: GameMode = GameMode.Easy,
    val usedPoemIds: Set<Int> = emptySet()
)

data class Poem(
    val id: Int,
    val title: String,
    val author: String,
    val dynasty: String,
    val content: String, // 完整诗句
    val keywords: List<String> // 用于翻牌的关键词（5个字）
)