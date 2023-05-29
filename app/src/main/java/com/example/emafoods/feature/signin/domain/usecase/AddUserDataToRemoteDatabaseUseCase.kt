package com.example.emafoods.feature.signin.domain.usecase

import com.example.emafoods.core.domain.models.UserData
import com.example.emafoods.core.domain.network.AuthService
import javax.inject.Inject

class AddUserDataToRemoteDatabaseUseCase @Inject constructor(
    private val authService: AuthService
) {

    suspend fun execute(userData: UserData) {
        authService.addUserDataToFirestore(userData)
    }
}