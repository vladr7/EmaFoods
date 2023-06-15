package com.example.emafoods.feature.generatefood.domain.models

import com.example.emafoods.core.presentation.models.FoodViewData

sealed class PreviousFoodResult<T> {
    data class Success<T>(val food: FoodViewData) : PreviousFoodResult<T>()
    data class SuccessAndStartOfTheList<T>(val food: FoodViewData) : PreviousFoodResult<T>()
    data class ErrorEmptyList<T>(val error: Unit) : PreviousFoodResult<T>()
}