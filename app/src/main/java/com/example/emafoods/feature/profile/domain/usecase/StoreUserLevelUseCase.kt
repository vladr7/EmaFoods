package com.example.emafoods.feature.profile.domain.usecase

import com.example.emafoods.feature.profile.domain.model.UserLevel
import com.example.emafoods.feature.profile.domain.repository.GameRepository
import javax.inject.Inject

class StoreUserLevelUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute(userLevel: UserLevel) =
        gameRepository.storeUserLevel(userLevel)
}