package com.example.emafoods.feature.profile.domain.usecase

import com.example.emafoods.feature.profile.domain.repository.GameRepository
import com.example.emafoods.feature.profile.presentation.game.model.LevelPermission
import javax.inject.Inject

class GetLevelPermissionsUseCase @Inject constructor(
    private val gameRepository: GameRepository
){

    fun execute(): List<LevelPermission> =
        gameRepository.levelPermissions()
}