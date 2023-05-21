package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.repository.GameRepository
import javax.inject.Inject

class CheckAppHasBeenOpenedEverUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute(): Boolean =
        gameRepository.appHasBeenOpenedEver()
}