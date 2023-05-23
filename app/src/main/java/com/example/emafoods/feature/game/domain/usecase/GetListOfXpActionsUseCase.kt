package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.repository.GameRepository
import javax.inject.Inject

class GetListOfXpActionsUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    fun execute(): List<String> =
        gameRepository.listOfXpActions()
}