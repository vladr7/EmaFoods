package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.repository.GameRepository
import javax.inject.Inject

class CheckAppOpenedTodayUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute(): Boolean {
        val appOpenedEver = gameRepository.appHasBeenOpenedEver()
        if(!appOpenedEver) {
            gameRepository.setAppOpenedToday()
        }
        return gameRepository.checkAppOpenedToday()
    }
}