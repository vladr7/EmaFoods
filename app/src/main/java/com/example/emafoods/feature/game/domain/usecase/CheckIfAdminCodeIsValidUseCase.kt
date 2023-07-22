package com.example.emafoods.feature.game.domain.usecase

import com.example.emafoods.feature.game.domain.repository.GameRepository
import javax.inject.Inject

class CheckIfAdminCodeIsValidUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun execute(adminCode: String): Boolean {
        val code = gameRepository.getAdminCode()
        return adminCode == code
    }
}