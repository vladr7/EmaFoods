package com.example.emafoods.feature.generatefood.domain.usecase

import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.generatefood.domain.models.GenerateResult
import javax.inject.Inject

class GenerateFoodUseCase @Inject constructor() {

    fun execute(
        categoryType: CategoryType,
        mainDishList: List<FoodViewData>,
        dessertList: List<FoodViewData>,
        breakfastList: List<FoodViewData>,
        soupList: List<FoodViewData>,
        indexMainDish: Int,
        indexDessert: Int,
        indexBreakfast: Int,
        indexSoup: Int
    ): GenerateResult<FoodViewData> {
        return when (categoryType) {
            CategoryType.MAIN_DISH -> {
                if(mainDishList.isEmpty()) {
                    GenerateResult.ErrorEmptyList(Unit)
                } else {
                    if(indexMainDish == mainDishList.size - 1) {
                        GenerateResult.SuccessAndIndexMustBeReset(mainDishList[0])
                    } else {
                        GenerateResult.Success(mainDishList[indexMainDish + 1])
                    }
                }
            }
            CategoryType.DESSERT -> {
                if (dessertList.isEmpty()) {
                    GenerateResult.ErrorEmptyList(Unit)
                } else {
                    if(indexDessert == dessertList.size - 1) {
                        GenerateResult.SuccessAndIndexMustBeReset(dessertList[0])
                    } else {
                        GenerateResult.Success(dessertList[indexDessert + 1])
                    }
                }
            }
            CategoryType.BREAKFAST -> {
                if(breakfastList.isEmpty()) {
                    GenerateResult.ErrorEmptyList(Unit)
                } else {
                    if(indexBreakfast == breakfastList.size - 1) {
                        GenerateResult.SuccessAndIndexMustBeReset(breakfastList[0])
                    } else {
                        GenerateResult.Success(breakfastList[indexBreakfast + 1])
                    }
                }
            }
            CategoryType.SOUP -> {
                if(soupList.isEmpty()) {
                    GenerateResult.ErrorEmptyList(Unit)
                } else {
                    if(indexSoup == soupList.size - 1) {
                        GenerateResult.SuccessAndIndexMustBeReset(soupList[0])
                    } else {
                        GenerateResult.Success(soupList[indexSoup + 1])
                    }
                }
            }
        }
    }
}