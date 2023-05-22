package com.example.emafoods.feature.game.data.datasource

import com.example.emafoods.core.domain.models.State
import com.example.emafoods.feature.game.domain.model.UserGameDetails
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.presentation.model.LevelPermission

interface GameDataSource {

    fun listOfXpActions(): List<String>
    suspend fun storeUserLevel(userLevel: UserLevel)
    fun levelPermissions(): List<LevelPermission>
    suspend fun storeUserXP(xp: Int)
    suspend fun userDetails(): UserGameDetails
    suspend fun storeXpToUnspent(xp: Int)
    suspend fun unspentUserXP(): Int
    suspend fun resetUnspentXp()
    suspend fun checkAppOpenedToday(): Boolean
    suspend fun setAppOpenedToday()
    suspend fun appHasBeenOpenedEver(): Boolean
    suspend fun addRewardToUserAcceptedRecipe(rewardedUserUid: String)
    suspend fun getUserRewards(): State<Long>
    suspend fun resetUserRewards()
}