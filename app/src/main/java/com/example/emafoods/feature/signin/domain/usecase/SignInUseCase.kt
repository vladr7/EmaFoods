package com.example.emafoods.feature.signin.domain.usecase

import com.example.emafoods.core.domain.network.AuthService
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authService: AuthService
) {

    suspend fun execute(idToken: String) =
        authService.signIn(idToken)
}