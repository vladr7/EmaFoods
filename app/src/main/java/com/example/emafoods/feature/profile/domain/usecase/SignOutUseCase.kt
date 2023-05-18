package com.example.emafoods.feature.profile.domain.usecase

import com.example.emafoods.core.domain.network.AuthService
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authService: AuthService
) {

    fun execute() = authService.signOut()
}