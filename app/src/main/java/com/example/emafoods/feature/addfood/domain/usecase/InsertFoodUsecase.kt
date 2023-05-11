package com.example.emafoods.feature.addfood.domain.usecase

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.utils.State
import javax.inject.Inject

class InsertFoodUseCase @Inject constructor(
    private val checkFieldsAreFilledUseCase: CheckFieldsAreFilledUseCase,
    private val foodRepository: FoodRepository
) {

    suspend fun execute(food: Food, imageUri: Uri?): State<Food> {
        if(!checkFieldsAreFilledUseCase.execute(food.description)) {
            return State.failed("Te rog adauga titlu si o scurta descriere")
        }

        if(imageUri == null) {
            return State.failed("Te rog adauga o poza cu reteta")
        }

        foodRepository.addFood(food)
        return foodRepository.addFoodImageToStorage(food, imageUri)
    }
}
