package com.example.emafoods.core.domain.network

import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.models.UserData

interface AuthService {

    fun isUserSignedIn(): Boolean
    fun getUserDetails(): UserData
    fun signOut()
    suspend fun signIn(idToken: String): State<Unit>
    suspend fun addUserDataToFirestore(userData: UserData)
    suspend fun addRewardToUser(rewardedUserUid: String)
}