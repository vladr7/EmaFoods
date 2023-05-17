package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.utils.State
import javax.inject.Inject

class AddFoodToPendingListUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {

    suspend fun execute(food: Food): State<Food> {
        foodRepository.addPendingFood(food)
        return foodRepository.addPendingFoodImageToStorage(food)
    }
}