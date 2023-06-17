package com.example.emafoods.feature.generatefood.domain.models

import com.example.emafoods.core.presentation.models.FoodViewData

sealed class GenerateResult<T> {
    data class Success<T>(val food: FoodViewData) : GenerateResult<T>()
    data class SuccessAndIndexMustBeReset<T>(val food: FoodViewData) : GenerateResult<T>()
    data class ErrorEmptyList<T>(val error: Unit) : GenerateResult<T>()
}