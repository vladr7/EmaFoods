package com.example.emafoods.feature.profile.presentation.game.model

data class Level(
    val levelNumber : Int,
    val remainingXpNeeded: Int,
    val hasAccess: Boolean,
    val permissions: List<Permission>
)
