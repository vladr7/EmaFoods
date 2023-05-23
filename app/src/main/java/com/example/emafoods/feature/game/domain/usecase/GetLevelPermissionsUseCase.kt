package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.repository.GameRepository
import com.example.emafoods.feature.game.presentation.model.LevelPermission
import javax.inject.Inject

class GetLevelPermissionsUseCase @Inject constructor(
    private val gameRepository: GameRepository
){

    fun execute(): List<LevelPermission> =
        gameRepository.levelPermissions()
}