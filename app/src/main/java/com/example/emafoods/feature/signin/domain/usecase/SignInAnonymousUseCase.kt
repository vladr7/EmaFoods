package com.example.emafoods.feature.signin.domain.usecase

import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.network.AuthService
import javax.inject.Inject

class SignInAnonymousUseCase @Inject constructor(
    private val authService: AuthService
) {

    suspend fun execute(): State<Unit> =
        authService.signInAnonymous()
}