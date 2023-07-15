package com.example.emafoods.feature.profile.domain.usecase

import com.example.emafoods.core.domain.network.AuthService
import javax.inject.Inject

class UpdateUsernameUseCase @Inject constructor(
    private val authService: AuthService
) {

    suspend fun execute(userName: String) {
        authService.updateUserName(userName)
    }
}