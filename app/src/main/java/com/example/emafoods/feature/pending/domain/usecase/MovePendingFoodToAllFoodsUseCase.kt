package com.example.emafoods.feature.pending.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.utils.State
import javax.inject.Inject

class MovePendingFoodToAllFoodsUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {

    suspend fun execute(food: Food): State<Food> {
        return foodRepository.movePendingFoodToAllFoods(food)
    }
}