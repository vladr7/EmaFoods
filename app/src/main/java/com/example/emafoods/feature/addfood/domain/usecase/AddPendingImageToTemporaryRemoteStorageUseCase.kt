package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import javax.inject.Inject

class AddPendingImageToTemporaryRemoteStorageUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
    private val getUserDetailsUseCase: GetUserDetailsUseCase
) {

    suspend fun execute(food: Food) {
        val userDetails = getUserDetailsUseCase.execute()
        val newFood = Food(
            imageRef = food.imageRef,
            authorUid = userDetails.uid,
        )
        foodRepository.addPendingFoodImageToTemporaryStorage(food = newFood)
    }
}