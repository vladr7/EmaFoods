package com.example.emafoods.feature.game.data.datasource

import com.example.emafoods.core.data.localstorage.LocalStorageKeys
import com.example.emafoods.core.domain.localstorage.LocalStorage
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.feature.game.domain.model.UserGameDetails
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.presentation.model.LevelPermission
import com.example.emafoods.feature.game.presentation.model.Permission
import javax.inject.Inject

class DefaultGameDataSource @Inject constructor(
    private val localStorage: LocalStorage,
    private val authService: AuthService
) : GameDataSource {

    override fun listOfXpActions() =
        listOf<String>(
            "Generare rețete",
            "Rețete adăugate",
            "Rețete acceptate de Admin",
            "Recenzie aplicație",
            "Numărul de foculețe",
        )

    override fun levelPermissions(): List<LevelPermission> =
        listOf(
            LevelPermission(
                level = UserLevel.LEVEL_1,
                permissions = listOf(
                    Permission.GENERATE,
                    Permission.ADD_TO_PENDING
                )
            ),
            LevelPermission(
                level = UserLevel.LEVEL_2,
                permissions = listOf(
                    Permission.MORE_AVATAR_ICONS
                )
            ),
            LevelPermission(
                level = UserLevel.LEVEL_3,
                permissions = listOf(
                    Permission.SURPRISE
                )
            ),
        )

    override suspend fun userDetails(): UserGameDetails {
        return UserGameDetails(
            userLevel = userLevel(),
            userXp = userXP(),
        )
    }

    override suspend fun storeXpToUnspent(xp: Long) {
        val currentUnspentXp = unspentUserXP()
        localStorage.putLong(LocalStorageKeys.XP_TO_UNSPENT, xp + currentUnspentXp)
    }

    override suspend fun unspentUserXP(): Long =
        localStorage.getLong(LocalStorageKeys.XP_TO_UNSPENT, defaultValue = 0L)

    override suspend fun resetUnspentXp() {
        localStorage.putInt(LocalStorageKeys.XP_TO_UNSPENT, 0)
    }

    override suspend fun consecutiveDaysAppOpened(): Int =
        localStorage.getInt(LocalStorageKeys.CONSECUTIVE_DAYS_APP_OPENED, defaultValue = 1)

    override suspend fun updateConsecutiveDaysAppOpened() {
        val currentConsecutiveDays = consecutiveDaysAppOpened()
        localStorage.putInt(LocalStorageKeys.CONSECUTIVE_DAYS_APP_OPENED, currentConsecutiveDays + 1)
    }

    override suspend fun resetConsecutiveDaysAppOpened() {
        localStorage.putInt(LocalStorageKeys.CONSECUTIVE_DAYS_APP_OPENED, 1)
    }

    override suspend fun addRewardToUserAcceptedRecipe(rewardedUserUid: String) {
        authService.addRewardToUser(rewardedUserUid)
    }

    override suspend fun getUserRewards(): State<Long> {
        return authService.getUserRewards()
    }

    override suspend fun resetUserRewards() {
        authService.resetUserRewards()
    }

    override suspend fun lastTimeUserOpenedApp(): Long {
        return localStorage.getLong(LocalStorageKeys.LAST_TIME_OPENED_APP, defaultValue = 0L)
    }

    override suspend fun updateLastTimeUserOpenedApp() {
        localStorage.putLong(LocalStorageKeys.LAST_TIME_OPENED_APP, System.currentTimeMillis())
    }

    override suspend fun getLastTimeUserReviewedApp(): Long {
        return localStorage.getLong(LocalStorageKeys.LAST_TIME_USER_REVIEWED_APP, defaultValue = 0L)
    }

    override suspend fun updateLastTimeUserReviewedApp() {
        localStorage.putLong(LocalStorageKeys.LAST_TIME_USER_REVIEWED_APP, System.currentTimeMillis())
    }

    override suspend fun upgradeBasicUserToAdmin() {
        authService.upgradeBasicUserToAdmin()
    }

    override suspend fun getAdminCode(): String {
        return authService.getAdminCode()
    }


    override suspend fun storeUserXP(xp: Long) {
        val currentXp = userXP()
        val xpToBeStored = currentXp + xp
        localStorage.putLong(LocalStorageKeys.USER_XP, xpToBeStored)
        authService.storeUserXP(xpToBeStored)
    }

    override suspend fun refreshUserXp() {
        val userXp = authService.getUserXP()
        if(userXp > 0L) {
            localStorage.putLong(LocalStorageKeys.USER_XP, userXp)
        }
    }

    private suspend fun userXP(): Long {
        return localStorage.getLong(LocalStorageKeys.USER_XP, defaultValue = 0L)
    }

    override suspend fun storeUserLevel(userLevel: UserLevel) {
        localStorage.putString(LocalStorageKeys.USER_LEVEL, userLevel.string)
        authService.storeUserLevel(userLevel)
    }

    private suspend fun userLevel(): UserLevel {
        val userLevelString = localStorage.getString(
            LocalStorageKeys.USER_LEVEL,
            defaultValue = UserLevel.LEVEL_1.string
        ) ?: UserLevel.LEVEL_1.string
        return UserLevel.fromString(userLevelString)
    }
}