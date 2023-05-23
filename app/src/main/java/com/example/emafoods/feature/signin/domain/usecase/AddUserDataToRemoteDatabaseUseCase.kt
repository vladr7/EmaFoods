package com.example.emafoods.feature.signin.domain.usecase

import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import javax.inject.Inject

class AddUserDataToRemoteDatabaseUseCase @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val authService: AuthService
) {

    suspend fun execute() {
        authService.addUserDataToFirestore(getUserDetailsUseCase.execute())
    }
}