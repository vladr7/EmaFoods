package com.example.emafoods.feature.game.domain.repository

import com.example.emafoods.feature.game.domain.model.UserGameDetails
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.presentation.model.LevelPermission

interface GameRepository {

    fun listOfXpActions(): List<String>
    suspend fun storeUserLevel(userLevel: UserLevel)
    fun levelPermissions(): List<LevelPermission>
    suspend fun storeUserXP(xp: Int)
    suspend fun userDetails(): UserGameDetails
}