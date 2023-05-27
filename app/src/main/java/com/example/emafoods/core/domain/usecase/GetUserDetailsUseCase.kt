package com.example.emafoods.core.domain.usecase

import com.example.emafoods.core.domain.models.UserData
import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.feature.game.domain.usecase.GetUserGameDetailsUseCase
import com.example.emafoods.feature.game.domain.usecase.GetUserRewardsUseCase
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(
    private val authService: AuthService,
    private val getUserGameDetailsUseCase: GetUserGameDetailsUseCase,
    private val getUserRewardsUseCase: GetUserRewardsUseCase
) {
    suspend fun execute(): UserData {
        val userRewards = getUserRewardsUseCase.execute()
        val userGameDetails = getUserGameDetailsUseCase.execute()
        val userData = authService.getUserDetails()
        return UserData(
            uid = userData.uid,
            email = userData.email,
            displayName = userData.displayName,
            awaitingRewards = userRewards,
            userXp = userGameDetails.userXp,
            userLevel = userGameDetails.userLevel
        )
    }
}