package com.example.emafoods.feature.game.domain.mapper

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
            val remainingXP = levelPermission.level.xp - userGameDetails.userXp
            ViewDataLevelPermission(
                level = levelPermission.level,
                permissions = levelPermission.permissions,
                hasAccess = levelPermission.level.xp <= userGameDetails.userXp,
                remainingXp = remainingXP
            )
        }
    }
}