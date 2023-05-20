package com.example.emafoods.feature.profile.domain.repository

import com.example.emafoods.feature.profile.domain.model.UserGameDetails
import com.example.emafoods.feature.profile.domain.model.UserLevel
import com.example.emafoods.feature.profile.presentation.game.model.LevelPermission

interface GameRepository {

    fun listOfXpActions(): List<String>
    suspend fun storeUserLevel(userLevel: UserLevel)
    fun levelPermissions(): List<LevelPermission>
    suspend fun storeUserXP(xp: Int)
    suspend fun userDetails(): UserGameDetails
}