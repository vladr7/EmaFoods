package com.example.emafoods.feature.game.domain.usecase

import javax.inject.Inject

class RefreshUserGameDetailsUseCase @Inject constructor(
    private val refreshUserXpUseCase: RefreshUserXpUseCase,
    private val getUserGameDetailsUseCase: GetUserGameDetailsUseCase,
    private val calculateUserLevelFromXpUseCase: CalculateUserLevelFromXpUseCase,
    private val storeUserLevelUseCase: StoreUserLevelUseCase
) {

    suspend fun execute() {
        refreshUserXpUseCase.execute()
        val userXp = getUserGameDetailsUseCase.execute().userXp
        val userLevel = calculateUserLevelFromXpUseCase.execute(userXp)
        storeUserLevelUseCase.execute(userLevel)
    }
}