package com.example.emafoods.feature.game.presentation.model

import com.example.emafoods.feature.game.domain.model.UserLevel

sealed class IncreaseXpResult<T> {
    data class ExceededUnspentThreshold<T>(val data: T) : IncreaseXpResult<T>()
    data class NotExceededUnspentThreshold<T>(val data: T) : IncreaseXpResult<T>()
    data class LeveledUp<T>(val levelAcquired: UserLevel) : IncreaseXpResult<T>()
}