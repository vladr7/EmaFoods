package com.example.emafoods.feature.game.data.datasource

import com.example.emafoods.core.domain.models.State
import com.example.emafoods.feature.game.domain.model.UserGameDetails
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.presentation.model.LevelPermission

interface GameDataSource {

    fun listOfXpActions(): List<String>
    suspend fun storeUserLevel(userLevel: UserLevel)
    fun levelPermissions(): List<LevelPermission>
    suspend fun storeUserXP(xp: Long)
    suspend fun refreshUserXp()
    suspend fun userDetails(): UserGameDetails
    suspend fun storeXpToUnspent(xp: Long)
    suspend fun unspentUserXP(): Long
    suspend fun resetUnspentXp()
    suspend fun consecutiveDaysAppOpened(): Int
    suspend fun updateConsecutiveDaysAppOpened()
    suspend fun resetConsecutiveDaysAppOpened()
    suspend fun addRewardToUserAcceptedRecipe(rewardedUserUid: String)
    suspend fun getUserRewards(): State<Long>
    suspend fun resetUserRewards()
    suspend fun lastTimeUserOpenedApp(): Long
    suspend fun updateLastTimeUserOpenedApp()
}