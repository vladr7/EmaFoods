package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.repository.FoodRepository
import javax.inject.Inject

class MoveTempImageToPendingUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {

    suspend fun execute(food: Food): State<Food> =
        foodRepository.moveTemporaryImageToPending(food = food)

}