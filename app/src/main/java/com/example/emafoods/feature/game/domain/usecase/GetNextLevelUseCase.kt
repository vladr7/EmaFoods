package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.model.UserLevel
import javax.inject.Inject

class GetNextLevelUseCase @Inject constructor(
    private val getUserGameDetailsUseCase: GetUserGameDetailsUseCase
) {

    suspend fun execute(): UserLevel {
        val userGameDetails = getUserGameDetailsUseCase.execute()
        return when(userGameDetails.userLevel) {
            UserLevel.LEVEL_1 -> UserLevel.LEVEL_2
            UserLevel.LEVEL_2 -> UserLevel.LEVEL_3
            UserLevel.LEVEL_3 -> UserLevel.LEVEL_4
            UserLevel.LEVEL_4 -> UserLevel.LEVEL_5
            UserLevel.LEVEL_5 -> UserLevel.LEVEL_5
        }
    }
}