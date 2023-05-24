package com.example.emafoods.feature.game.presentation.model

import com.example.emafoods.feature.game.domain.model.UserLevel

data class LevelPermission(
    val level: UserLevel,
    val permissions: List<Permission>
)
