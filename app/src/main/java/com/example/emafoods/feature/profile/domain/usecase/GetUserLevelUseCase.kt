package com.example.emafoods.feature.profile.domain.usecase

import com.example.emafoods.feature.profile.domain.model.UserLevel
import com.example.emafoods.feature.profile.domain.repository.GameRepository
import javax.inject.Inject

class GetUserLevelUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    fun execute(): UserLevel =
        gameRepository.userLevel()
}