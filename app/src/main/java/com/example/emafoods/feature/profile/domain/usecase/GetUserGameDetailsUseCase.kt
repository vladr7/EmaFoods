package com.example.emafoods.feature.profile.domain.usecase

import com.example.emafoods.feature.profile.domain.model.UserGameDetails
import com.example.emafoods.feature.profile.domain.repository.GameRepository
import javax.inject.Inject

class GetUserGameDetailsUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute(): UserGameDetails =
        gameRepository.userDetails()
}