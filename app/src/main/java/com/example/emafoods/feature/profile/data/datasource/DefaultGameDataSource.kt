package com.example.emafoods.feature.profile.data.datasource

import com.example.emafoods.core.data.localstorage.LocalStorage
import com.example.emafoods.core.data.localstorage.LocalStorageKeys
import com.example.emafoods.feature.profile.domain.model.UserLevel
import javax.inject.Inject

class DefaultGameDataSource @Inject constructor(
    private val localStorage: LocalStorage
) : GameDataSource {
    override fun listOfXpActions() =
        listOf<String>(
            "Generare reteta",
            "Reteta adaugata",
            "Reteta acceptata de admin",
            "Review aplicatie",
            "Deschizi aplicatia pentru prima data pe ziua respectiva",
            "Accepti/respingi o reteta (admin)",
        )

    override suspend fun userLevel(): UserLevel {
        val userLevelString = localStorage.getString(LocalStorageKeys.USER_LEVEL, defaultValue = UserLevel.PLEB.string) ?: UserLevel.PLEB.string
        return UserLevel.fromString(userLevelString)
    }

    override suspend fun storeUserLevel(userLevel: UserLevel) {
        localStorage.putString(LocalStorageKeys.USER_LEVEL, userLevel.string)
    }
}