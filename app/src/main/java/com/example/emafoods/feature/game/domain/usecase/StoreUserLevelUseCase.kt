package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.domain.repository.GameRepository
import javax.inject.Inject

class StoreUserLevelUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute(userLevel: UserLevel) =
        gameRepository.storeUserLevel(userLevel)
}