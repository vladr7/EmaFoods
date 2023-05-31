package com.example.emafoods.feature.generatefood.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.presentation.models.FoodViewData
import javax.inject.Inject

class GenerateFoodUseCase @Inject constructor() {

    fun execute(previousFood: FoodViewData, foods: List<Food>): Food {
        if(foods.isEmpty()) return Food(id = "empty")
        var nextFood = foods.random()
        while(previousFood.id == nextFood.id) {
           nextFood = foods.random()
        }
        return nextFood
    }
}