package com.example.emafoods.core.domain.models

import com.example.emafoods.feature.game.domain.model.UserLevel

data class UserData(
    val uid: String,
    val email: String,
    val displayName: String,
    val awaitingRewards: Long = 0,
    val userXp: Long = 0,
    val userLevel: UserLevel = UserLevel.LEVEL_1,
)