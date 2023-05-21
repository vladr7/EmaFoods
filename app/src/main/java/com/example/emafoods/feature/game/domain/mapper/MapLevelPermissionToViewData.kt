package com.example.emafoods.feature.game.domain.mapper

import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.domain.usecase.GetUserGameDetailsUseCase
import com.example.emafoods.feature.game.presentation.ViewDataLevelPermission
import com.example.emafoods.feature.game.presentation.model.LevelPermission
import javax.inject.Inject

class MapLevelPermissionToViewData @Inject constructor(
    private val getUserGameDetailsUseCase: GetUserGameDetailsUseCase,
) {

    suspend fun execute(levelPermissions: List<LevelPermission>): List<ViewDataLevelPermission> {
        val userGameDetails = getUserGameDetailsUseCase.execute()
        return levelPermissions.map { levelPermission ->
            val remainingXP = getLevelFromPermissionLevel(levelPermission).xp - userGameDetails.userXp
            ViewDataLevelPermission(
                levelName = levelPermission.levelName,
                permissions = levelPermission.permissions,
                hasAccess = userGameDetails.userLevel.string == levelPermission.levelName,
                remainingXp = remainingXP
            )
        }
    }

    private fun getLevelFromPermissionLevel(permissionLevel: LevelPermission): UserLevel {
        return when(permissionLevel.levelName) {
            UserLevel.LEVEL_1.string -> UserLevel.LEVEL_1
            UserLevel.LEVEL_2.string -> UserLevel.LEVEL_2
            UserLevel.LEVEL_3.string -> UserLevel.LEVEL_3
            UserLevel.LEVEL_4.string -> UserLevel.LEVEL_4
            UserLevel.LEVEL_5.string -> UserLevel.LEVEL_5
            else -> UserLevel.LEVEL_1
        }
    }
}