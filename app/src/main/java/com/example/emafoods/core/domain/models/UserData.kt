package com.example.emafoods.core.domain.models

import com.example.emafoods.feature.game.domain.model.UserLevel
import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.SerialName

data class UserData(
    @DocumentId
    val uid: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("displayName")
    val displayName: String = "",
    @SerialName("awaitingRewards")
    val awaitingRewards: Long = 0,
    @SerialName("userXp")
    val userXp: Long = 0,
    @SerialName("userLevel")
    val userLevel: String = UserLevel.LEVEL_1.string,
    @SerialName("admin")
    val admin: Boolean = false,
    @SerialName("consecutiveDaysAppOpened")
    val consecutiveDaysAppOpened: Int = 0
)