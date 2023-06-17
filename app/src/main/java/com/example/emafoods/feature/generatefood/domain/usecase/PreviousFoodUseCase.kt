package com.example.emafoods.feature.generatefood.domain.usecase

import com.example.emafoods.core.presentation.models.FoodViewData
import javax.inject.Inject

class PreviousFoodUseCase @Inject constructor() {

    fun execute(foodList: List<FoodViewData>, index: Int): FoodViewData {
        if(index == 0) {
            return foodList[foodList.size - 1]
        }
        return foodList[index - 1]
    }
}