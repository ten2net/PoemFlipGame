package com.example.poemflipgame.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poemflipgame.data.PoemRepository
import com.example.poemflipgame.model.GameMode
import com.example.poemflipgame.model.GameState
import com.example.poemflipgame.model.PoemCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val poemRepository: PoemRepository = PoemRepository
) : ViewModel() {
    
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    private var countDownTimer: CountDownTimer? = null
    private val gameTime = 60000L // 60秒
    private val maxFlips = 5
    
    private val usedPoemIds = mutableSetOf<Int>()
    
    fun startNewGame(mode: GameMode) {
        countDownTimer?.cancel()
        
        val poem = poemRepository.getRandomPoem(usedPoemIds)
        usedPoemIds.add(poem.id)
        
        // 如果已经用了超过100首，清空记录
        if (usedPoemIds.size >= 100) {
            usedPoemIds.clear()
        }
        
        val gridSize = when (mode) {
            GameMode.Easy -> 9  // 3x3
            GameMode.Hard -> 25 // 5x5
        }
        
        // 创建卡片：5张是诗句字，其余是干扰字
        val cards = mutableListOf<PoemCard>()
        val keywords = poem.keywords
        
        // 添加5张诗句字卡
        keywords.forEachIndexed { index, char ->
            cards.add(PoemCard(index, char[0], isFlipped = false, isMatched = false))
        }
        
        // 添加干扰字卡
        val distractorChars = generateDistractorChars(gridSize - 5, keywords)
        distractorChars.forEachIndexed { index, char ->
            cards.add(PoemCard(index + 5, char, isFlipped = false, isMatched = false))
        }
        
        // 打乱卡片顺序
        cards.shuffle()
        
        _gameState.value = GameState(
            cards = cards,
            timeRemaining = 60,
            isGameActive = true,
            isGameOver = false,
            isSuccess = false,
            currentPoem = poem,
            gameMode = mode,
            usedPoemIds = usedPoemIds.toSet()
        )
        
        startTimer()
    }
    
    private fun generateDistractorChars(count: Int, excludeKeywords: List<String>): List<Char> {
        val commonChars = "的一是在不了有和人这中大为上个国我以要他时来用们生到作地于出就分对成会可主发年动同工也能下过子说产种面而方后多定行学法所民得经十三之进着等部度家电力里如水化高自二理起小物现实加量都两体制机当使点从业本去把性好应开它合还因由其些然前外天政四日那社义事平形相全表间样与关各重新线内数正心反你明看原又么利比或但质气第向道命此变条只没结解问意建月公无系军很情者最立代想已通并提直题党程展五果料象员革位入常文总次品式活设及管特件长求老头基资边流路级少图山统接知较将组见计别她手角期根论运农指几九区强放决西被干做必战先回则任取完举色"
        val excludeSet = excludeKeywords.flatMap { it.toList() }.toSet()
        val availableChars = commonChars.filter { it !in excludeSet }
        return availableChars.shuffled().take(count)
    }
    
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(gameTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _gameState.update { it.copy(timeRemaining = (millisUntilFinished / 1000).toInt()) }
            }
            
            override fun onFinish() {
                endGame(false)
            }
        }.start()
    }
    
    fun flipCard(cardId: Int) {
        val currentState = _gameState.value
        
        if (!currentState.isGameActive || currentState.isGameOver) return
        if (currentState.flippedCount >= maxFlips) return
        
        val card = currentState.cards.find { it.id == cardId } ?: return
        if (card.isFlipped || card.isMatched) return
        
        val updatedCards = currentState.cards.map { 
            if (it.id == cardId) it.copy(isFlipped = true) else it 
        }
        
        val newFlippedCount = currentState.flippedCount + 1
        
        _gameState.update { 
            it.copy(
                cards = updatedCards,
                flippedCount = newFlippedCount,
                selectedCards = it.selectedCards + cardId
            )
        }
        
        // 翻转了5张，检查是否成功
        if (newFlippedCount >= maxFlips) {
            checkWinCondition()
        }
    }
    
    private fun checkWinCondition() {
        val currentState = _gameState.value
        val poem = currentState.currentPoem ?: return
        
        val flippedChars = currentState.cards
            .filter { it.isFlipped }
            .map { it.char.toString() }
        
        val keywords = poem.keywords
        
        // 检查翻出的5个字是否正好是诗句的5个关键字
        val isSuccess = flippedChars.sorted() == keywords.sorted()
        
        endGame(isSuccess)
    }
    
    private fun endGame(success: Boolean) {
        countDownTimer?.cancel()
        _gameState.update { 
            it.copy(
                isGameActive = false,
                isGameOver = true,
                isSuccess = success
            )
        }
    }
    
    fun resetGame() {
        countDownTimer?.cancel()
        _gameState.value = GameState()
    }
    
    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }
}