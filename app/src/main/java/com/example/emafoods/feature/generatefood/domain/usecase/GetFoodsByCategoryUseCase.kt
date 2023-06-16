package com.example.emafoods.feature.generatefood.domain.usecase

import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import javax.inject.Inject

class GetFoodsByCategoryUseCase @Inject constructor() {

    fun execute(foods: List<FoodViewData>, categoryType: CategoryType): List<FoodViewData> {
        return foods.filter { food ->
            food.categoryType == categoryType
        }.shuffled()
    }
}