package com.example.emafoods.feature.profile.data.repository

import com.example.emafoods.feature.profile.data.datasource.GameDataSource
import com.example.emafoods.feature.profile.domain.model.UserGameDetails
import com.example.emafoods.feature.profile.domain.model.UserLevel
import com.example.emafoods.feature.profile.domain.repository.GameRepository
import com.example.emafoods.feature.profile.presentation.game.model.LevelPermission
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
}
