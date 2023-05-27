package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.repository.GameRepository
import javax.inject.Inject

class CheckXNrOfDaysPassedSinceLastReviewUseCase @Inject constructor(
    private val gameRepository: GameRepository
){
    suspend fun execute(days: Int): Boolean {
        val lastTimeReviewed = gameRepository.getLastTimeUserReviewedApp()
        val currentTime = System.currentTimeMillis()
        val daysPassed = (currentTime - lastTimeReviewed) / (1000 * 60 * 60 * 24)
        return daysPassed >= days
    }
}