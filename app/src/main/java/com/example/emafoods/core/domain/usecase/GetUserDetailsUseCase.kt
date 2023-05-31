package com.example.emafoods.core.domain.usecase

import com.example.emafoods.core.domain.models.UserData
import com.example.emafoods.core.domain.network.AuthService
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(
    private val authService: AuthService,
) {
    suspend fun execute(): UserData {
        val userData = authService.getUserDetails()
        return userData
    }
}