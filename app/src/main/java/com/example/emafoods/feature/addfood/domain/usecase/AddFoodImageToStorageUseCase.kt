package com.example.emafoods.feature.addfood.domain.usecase

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.repository.FoodRepository
import javax.inject.Inject

class AddFoodImageToStorageUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
) {
    suspend fun execute(food: Food, fileUri: Uri) {
        foodRepository.addFoodImageToStorage(food, fileUri)
    }
}