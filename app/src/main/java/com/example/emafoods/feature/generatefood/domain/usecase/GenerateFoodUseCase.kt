package com.example.emafoods.feature.generatefood.domain.usecase

import com.example.emafoods.core.presentation.models.FoodViewData
import javax.inject.Inject

class GenerateFoodUseCase @Inject constructor() {

    fun execute(foodList: List<FoodViewData>, index: Int): FoodViewData {
        if(index >= foodList.size - 1) {
            return foodList[0]
        }
        return foodList[index + 1]
    }


}