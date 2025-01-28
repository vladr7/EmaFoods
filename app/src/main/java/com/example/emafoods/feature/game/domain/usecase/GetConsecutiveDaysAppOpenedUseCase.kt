package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.repository.GameRepository
import javax.inject.Inject

class GetConsecutiveDaysAppOpenedUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute(): Long {
        return gameRepository.consecutiveDaysAppOpened()
    }
}