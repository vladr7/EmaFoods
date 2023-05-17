package com.example.emafoods.feature.pending.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.repository.FoodRepository
import javax.inject.Inject

class DeletePendingFoodUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {

    suspend fun execute(food: Food) =
        foodRepository.deletePendingFood(food)
}