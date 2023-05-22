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
): GameRepository {

    override fun listOfXpActions(): List<String> =
        gameDataSource.listOfXpActions()

    override suspend fun storeUserLevel(userLevel: UserLevel) =
        gameDataSource.storeUserLevel(userLevel)

    override fun levelPermissions(): List<LevelPermission> =
        gameDataSource.levelPermissions()

    override suspend fun storeUserXP(xp: Int) {
        gameDataSource.storeUserXP(xp)
    }

    override suspend fun userDetails(): UserGameDetails =
        gameDataSource.userDetails()

    override suspend fun storeXpToUnspent(xp: Int) =
        gameDataSource.storeXpToUnspent(xp)

    override suspend fun unspentUserXP(): Int =
        gameDataSource.unspentUserXP()

    override suspend fun resetUnspentXp() {
        gameDataSource.resetUnspentXp()
    }

    override suspend fun checkAppOpenedToday(): Boolean =
        gameDataSource.checkAppOpenedToday()

    override suspend fun setAppOpenedToday() {
        gameDataSource.setAppOpenedToday()
    }

    override suspend fun appHasBeenOpenedEver(): Boolean {
        return gameDataSource.appHasBeenOpenedEver()
    }

    override suspend fun addRewardToUserAcceptedRecipe(rewardedUserUid: String) {
        gameDataSource.addRewardToUserAcceptedRecipe(rewardedUserUid)
    }

    override suspend fun getUserRewards(): Long =
        when(val result = gameDataSource.getUserRewards()) {
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

}
