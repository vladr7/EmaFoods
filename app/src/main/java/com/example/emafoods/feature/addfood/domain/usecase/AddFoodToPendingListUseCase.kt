package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.repository.FoodRepository
import javax.inject.Inject

class AddFoodToPendingListUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
    private val moveTempImageToPendingUseCase: MoveTempImageToPendingUseCase
) {

    suspend fun execute(food: Food, shouldAddImageFromTemporary: Boolean): State<Food> {
        foodRepository.addPendingFood(food)
        return if(!shouldAddImageFromTemporary) {
            foodRepository.addPendingFoodImageToStorage(food)
        } else {
            moveTempImageToPendingUseCase.execute(food)
        }
    }
}