package com.example.emafoods.feature.profile.domain.repository

import com.example.emafoods.feature.profile.domain.model.UserLevel

interface GameRepository {

    fun listOfXpActions(): List<String>
    suspend fun userLevel(): UserLevel
    suspend fun storeUserLevel(userLevel: UserLevel)
}