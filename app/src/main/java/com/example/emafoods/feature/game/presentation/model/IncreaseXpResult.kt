package com.example.emafoods.feature.game.presentation.model

sealed class IncreaseXpResult<T> {
    data class ExceededThreshold<T>(val data: T) : IncreaseXpResult<T>()
    data class NotExceededThreshold<T>(val data: T) : IncreaseXpResult<T>()
}