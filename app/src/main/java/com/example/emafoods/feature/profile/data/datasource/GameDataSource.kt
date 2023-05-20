package com.example.emafoods.feature.profile.data.datasource

import com.example.emafoods.feature.profile.domain.model.UserLevel

interface GameDataSource {

    fun listOfXpActions(): List<String>
    suspend fun userLevel(): UserLevel
    suspend fun storeUserLevel(userLevel: UserLevel)
}