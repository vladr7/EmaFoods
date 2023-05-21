package com.example.emafoods.feature.game.data.datasource

import com.example.emafoods.core.data.localstorage.LocalStorage
import com.example.emafoods.core.data.localstorage.LocalStorageKeys
import com.example.emafoods.feature.game.domain.model.UserGameDetails
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.presentation.model.LevelPermission
import com.example.emafoods.feature.game.presentation.model.Permission
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

    override suspend fun storeUserLevel(userLevel: UserLevel) {
        localStorage.putString(LocalStorageKeys.USER_LEVEL, userLevel.string)
    }

    override fun levelPermissions(): List<LevelPermission> =
        listOf(
            LevelPermission(
                levelName = UserLevel.LEVEL_1.string,
                permissions = listOf(
                    Permission.GENERATE,
                    Permission.ADD_TO_PENDING
                )
            ),
            LevelPermission(
                levelName = UserLevel.LEVEL_2.string,
                permissions = listOf(
                    Permission.ACCEPT_DENY_FROM_PENDING
                )
            ),
            LevelPermission(
                levelName = UserLevel.LEVEL_3.string,
                permissions = listOf(
                    Permission.MAIN_LIST_VISIBLE
                )
            ),
            LevelPermission(
                levelName = UserLevel.LEVEL_4.string,
                permissions = listOf(
                    Permission.EDIT_MAIN
                )
            ),
            LevelPermission(
                levelName = UserLevel.LEVEL_5.string,
                permissions = listOf(
                    Permission.SURPRISE
                )
            )
        )

    override suspend fun userDetails(): UserGameDetails {
        return UserGameDetails(
            userLevel = userLevel(),
            userXP = userXP(),
        )
    }

    override suspend fun storeXpToUnspent(xp: Int) {
        val currentUnspentXp = unspentUserXP()
        localStorage.putInt(LocalStorageKeys.XP_TO_UNSPENT, xp + currentUnspentXp)
    }

    override suspend fun unspentUserXP(): Int =
        localStorage.getInt(LocalStorageKeys.XP_TO_UNSPENT, defaultValue = 0)

    override suspend fun resetUnspentXp() {
        localStorage.putInt(LocalStorageKeys.XP_TO_UNSPENT, 0)
    }

    override suspend fun storeUserXP(xp: Int) {
        val currentXp = userXP()
        localStorage.putInt(LocalStorageKeys.USER_XP, xp + currentXp)
    }

    private suspend fun userXP(): Int {
        return localStorage.getInt(LocalStorageKeys.USER_XP, defaultValue = 0)
    }

    private suspend fun userLevel(): UserLevel {
        val userLevelString = localStorage.getString(
            LocalStorageKeys.USER_LEVEL,
            defaultValue = UserLevel.LEVEL_1.string
        ) ?: UserLevel.LEVEL_1.string
        return UserLevel.fromString(userLevelString)
    }
}