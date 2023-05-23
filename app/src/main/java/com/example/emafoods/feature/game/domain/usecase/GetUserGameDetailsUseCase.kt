package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.model.UserGameDetails
import com.example.emafoods.feature.game.domain.repository.GameRepository
import javax.inject.Inject

class GetUserGameDetailsUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute(): UserGameDetails =
        gameRepository.userDetails()
}