package com.example.emafoods.feature.addfood.domain.usecase

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.utils.State
import javax.inject.Inject

class AddFoodToMainListUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
) {

    suspend fun execute(food: Food, imageUri: Uri): State<Food> {
        foodRepository.addFood(food)
        return foodRepository.addFoodImageToStorage(food, fileUri =  imageUri)
    }
}