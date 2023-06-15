package com.example.emafoods.feature.generatefood.domain.usecase

import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.generatefood.domain.models.PreviousFoodResult
import javax.inject.Inject

class PreviousFoodUseCase @Inject constructor() {
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
    ): PreviousFoodResult<FoodViewData> =
        when (categoryType) {
            CategoryType.MAIN_DISH -> {
                if (mainDishList.isEmpty()) {
                    PreviousFoodResult.ErrorEmptyList(Unit)
                } else {
                    if (indexMainDish <= 1) {
                        PreviousFoodResult.SuccessAndStartOfTheList(mainDishList[0])
                    } else {
                        PreviousFoodResult.Success(mainDishList[indexMainDish - 1])
                    }
                }
            }
            CategoryType.DESSERT -> {
                if (dessertList.isEmpty()) {
                    PreviousFoodResult.ErrorEmptyList(Unit)
                } else {
                    if (indexDessert <= 1) {
                        PreviousFoodResult.SuccessAndStartOfTheList(dessertList[0])
                    } else {
                        PreviousFoodResult.Success(dessertList[indexDessert - 1])
                    }
                }
            }
            CategoryType.BREAKFAST -> {
                if (breakfastList.isEmpty()) {
                    PreviousFoodResult.ErrorEmptyList(Unit)
                } else {
                    if (indexBreakfast <= 1) {
                        PreviousFoodResult.SuccessAndStartOfTheList(breakfastList[0])
                    } else {
                        PreviousFoodResult.Success(breakfastList[indexBreakfast - 1])
                    }
                }
            }
            CategoryType.SOUP -> {
                if (soupList.isEmpty()) {
                    PreviousFoodResult.ErrorEmptyList(Unit)
                } else {
                    if (indexSoup <= 1) {
                        PreviousFoodResult.SuccessAndStartOfTheList(soupList[0])
                    } else {
                        PreviousFoodResult.Success(soupList[indexSoup - 1])
                    }
                }
            }
        }
}