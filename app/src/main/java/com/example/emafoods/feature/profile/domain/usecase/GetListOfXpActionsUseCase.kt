package com.example.emafoods.feature.profile.domain.usecase

import com.example.emafoods.feature.profile.domain.usecase.repository.GameRepository
import javax.inject.Inject

class GetListOfXpActionsUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    fun execute(): List<String> =
        gameRepository.listOfXpActions()
}