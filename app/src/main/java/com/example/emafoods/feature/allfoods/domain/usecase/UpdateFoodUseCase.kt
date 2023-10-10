package com.example.emafoods.feature.allfoods.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.repository.FoodRepository
import javax.inject.Inject

class UpdateFoodUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {

    suspend fun execute(food: Food) {
        foodRepository.updateFood(food)
    }
}