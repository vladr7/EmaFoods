package com.example.emafoods.feature.game.data.repository

import com.example.emafoods.core.domain.models.State
import com.example.emafoods.feature.game.data.datasource.GameDataSource
import com.example.emafoods.feature.game.domain.model.UserGameDetails
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.domain.repository.GameRepository
import com.example.emafoods.feature.game.presentation.model.LevelPermission
import javax.inject.Inject

class DefaultGameRepository @Inject constructor(
    private val gameDataSource: GameDataSource
) : GameRepository {

    override fun listOfXpActions(): List<String> =
        gameDataSource.listOfXpActions()

    override suspend fun storeUserLevel(userLevel: UserLevel) =
        gameDataSource.storeUserLevel(userLevel)

    override fun levelPermissions(): List<LevelPermission> =
        gameDataSource.levelPermissions()

    override suspend fun storeUserXP(xp: Long) {
        gameDataSource.storeUserXP(xp)
    }

    override suspend fun refreshUserXP() {
        gameDataSource.refreshUserXp()
    }

    override suspend fun userDetails(): UserGameDetails =
        gameDataSource.userDetails()

    override suspend fun storeXpToUnspent(xp: Long) =
        gameDataSource.storeXpToUnspent(xp)

    override suspend fun unspentUserXP(): Long =
        gameDataSource.unspentUserXP()

    override suspend fun resetUnspentXp() {
        gameDataSource.resetUnspentXp()
    }

    override suspend fun consecutiveDaysAppOpened(): Long {
        return gameDataSource.consecutiveDaysAppOpened()
    }

    override suspend fun updateConsecutiveDaysAppOpened() {
        gameDataSource.updateConsecutiveDaysAppOpened()
    }

    override suspend fun resetConsecutiveDaysAppOpened() {
        gameDataSource.resetConsecutiveDaysAppOpened()
    }

    override suspend fun addRewardToUserAcceptedRecipe(rewardedUserUid: String) {
        gameDataSource.addRewardToUserAcceptedRecipe(rewardedUserUid)
    }

    override suspend fun getUserRewards(): Long =
        when (val result = gameDataSource.getUserRewards()) {
            is State.Failed -> {
                0L
            }

            is State.Success -> {
                result.data
            }
        }

    override suspend fun resetUserRewards() {
        gameDataSource.resetUserRewards()
    }

    override suspend fun lastTimeUserOpenedApp(): Long {
        return gameDataSource.lastTimeUserOpenedApp()
    }

    override suspend fun updateLastTimeUserOpenedApp() {
        gameDataSource.updateLastTimeUserOpenedApp()
    }

    override suspend fun getLastTimeUserReviewedApp(): Long {
        return gameDataSource.getLastTimeUserReviewedApp()
    }

    override suspend fun updateLastTimeUserReviewedApp() {
        gameDataSource.updateLastTimeUserReviewedApp()
    }

    override suspend fun upgradeBasicUserToAdmin() {
        gameDataSource.upgradeBasicUserToAdmin()
    }

    override suspend fun getAdminCode(): String {
        return gameDataSource.getAdminCode()
    }
}
