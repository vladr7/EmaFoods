package com.example.emafoods.feature.game.domain.usecase

import javax.inject.Inject

class CheckUserLeveledUpUseCase @Inject constructor(
    private val getUserGameDetailsUseCase: GetUserGameDetailsUseCase,
    private val nextLevelUseCase: GetNextLevelUseCase
) {
    suspend fun execute(unspentXp: Long): Boolean {
        val userGameDetails = getUserGameDetailsUseCase.execute()
        val currentUserXp = userGameDetails.userXp
        val nextLevel = nextLevelUseCase.execute()
        if(nextLevel == userGameDetails.userLevel) return false
        return currentUserXp + unspentXp >= nextLevel.xp
    }
}