package com.example.emafoods.core.domain.network

import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.models.UserData
import com.example.emafoods.feature.game.domain.model.UserLevel

interface AuthService {

    fun isUserSignedIn(): Boolean
    suspend fun getUserDetails(): UserData
    fun signOut()
    suspend fun signInGoogle(idToken: String): State<Unit>
    suspend fun signInAnonymous(): State<Unit>
    suspend fun addUserDataToFirestore(userData: UserData)
    suspend fun addRewardToUser(rewardedUserUid: String)
    suspend fun getUserRewards(): State<Long>
    suspend fun resetUserRewards()
    suspend fun storeUserXP(xpToBeStored: Long)
    suspend fun getUserXP(): Long
    suspend fun storeUserLevel(userLevel: UserLevel)
    suspend fun upgradeBasicUserToAdmin()
}