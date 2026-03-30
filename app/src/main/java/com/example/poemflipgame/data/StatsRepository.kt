package com.example.poemflipgame.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "game_stats")

data class GameStats(
    val totalGames: Int = 0,
    val successCount: Int = 0,
    val failureCount: Int = 0,
    val totalTimeSeconds: Long = 0,
    val bestTimeSeconds: Long = Long.MAX_VALUE,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0
)

class StatsRepository(private val context: Context) {
    
    companion object {
        val TOTAL_GAMES = intPreferencesKey("total_games")
        val SUCCESS_COUNT = intPreferencesKey("success_count")
        val FAILURE_COUNT = intPreferencesKey("failure_count")
        val TOTAL_TIME = longPreferencesKey("total_time")
        val BEST_TIME = longPreferencesKey("best_time")
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val LONGEST_STREAK = intPreferencesKey("longest_streak")
    }
    
    val stats: Flow<GameStats> = context.dataStore.data.map { preferences ->
        GameStats(
            totalGames = preferences[TOTAL_GAMES] ?: 0,
            successCount = preferences[SUCCESS_COUNT] ?: 0,
            failureCount = preferences[FAILURE_COUNT] ?: 0,
            totalTimeSeconds = preferences[TOTAL_TIME] ?: 0,
            bestTimeSeconds = preferences[BEST_TIME] ?: Long.MAX_VALUE,
            currentStreak = preferences[CURRENT_STREAK] ?: 0,
            longestStreak = preferences[LONGEST_STREAK] ?: 0
        )
    }
    
    suspend fun recordGameSuccess(timeSeconds: Long) {
        context.dataStore.edit { preferences ->
            val totalGames = (preferences[TOTAL_GAMES] ?: 0) + 1
            val successCount = (preferences[SUCCESS_COUNT] ?: 0) + 1
            val totalTime = (preferences[TOTAL_TIME] ?: 0) + timeSeconds
            val currentStreak = (preferences[CURRENT_STREAK] ?: 0) + 1
            val longestStreak = maxOf(preferences[LONGEST_STREAK] ?: 0, currentStreak)
            val bestTime = if (timeSeconds < (preferences[BEST_TIME] ?: Long.MAX_VALUE)) {
                timeSeconds
            } else {
                preferences[BEST_TIME] ?: Long.MAX_VALUE
            }
            
            preferences[TOTAL_GAMES] = totalGames
            preferences[SUCCESS_COUNT] = successCount
            preferences[TOTAL_TIME] = totalTime
            preferences[CURRENT_STREAK] = currentStreak
            preferences[LONGEST_STREAK] = longestStreak
            preferences[BEST_TIME] = bestTime
        }
    }
    
    suspend fun recordGameFailure() {
        context.dataStore.edit { preferences ->
            val totalGames = (preferences[TOTAL_GAMES] ?: 0) + 1
            val failureCount = (preferences[FAILURE_COUNT] ?: 0) + 1
            
            preferences[TOTAL_GAMES] = totalGames
            preferences[FAILURE_COUNT] = failureCount
            preferences[CURRENT_STREAK] = 0
        }
    }
}