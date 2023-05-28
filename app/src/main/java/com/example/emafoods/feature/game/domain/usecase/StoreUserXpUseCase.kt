package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.repository.GameRepository
import javax.inject.Inject

class StoreUserXpUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute(userXp: Long) {
        gameRepository.storeUserXP(userXp)
    }
}