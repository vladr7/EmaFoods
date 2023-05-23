package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.repository.GameRepository
import javax.inject.Inject

class ResetConsecutiveDaysAppOpenedUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute() {
        return gameRepository.resetConsecutiveDaysAppOpened()
    }
}