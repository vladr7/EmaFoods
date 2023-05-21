package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.feature.game.domain.repository.GameRepository
import javax.inject.Inject

class AddRewardToUserAcceptedRecipeUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute(food: Food) {
        val rewardedUserUid = food.authorUid
        gameRepository.addRewardToUserAcceptedRecipe(rewardedUserUid)
    }
}