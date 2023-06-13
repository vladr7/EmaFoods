package com.example.emafoods.feature.addfood.domain.models

sealed class IngredientResult<T> {
    data class Success<T>(val data: T) : IngredientResult<T>()
    data class ErrorAlreadyAdded<T>(val unit: Unit) : IngredientResult<T>()
}