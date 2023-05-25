package com.example.emafoods.feature.pending.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.repository.FoodRepository
import javax.inject.Inject

class DeletePendingFoodUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {

    suspend fun execute(food: Food) : State<Food> {
        if(food.id.isEmpty()) {
            return State.failed("Nu mai exista reteta in asteptare")
        }
        return foodRepository.deletePendingFood(food)
    }
}