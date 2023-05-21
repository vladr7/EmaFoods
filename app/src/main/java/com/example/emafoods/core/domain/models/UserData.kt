package com.example.emafoods.core.domain.models

data class UserData(
    val uid: String,
    val email: String,
    val displayName: String,
    val userType: UserType = UserType.BASIC,
    val awaitingRewards: Long = 0,
)