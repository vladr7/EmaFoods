package com.example.emafoods.feature.signin.domain.usecase

import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.network.AuthService
import javax.inject.Inject

class SignInGoogleUseCase @Inject constructor(
    private val authService: AuthService
) {

    suspend fun execute(idToken: String): State<Unit> =
        authService.signInGoogle(idToken)
}