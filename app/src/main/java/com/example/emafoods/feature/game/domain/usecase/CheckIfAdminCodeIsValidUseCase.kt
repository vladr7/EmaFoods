package com.example.emafoods.feature.game.domain.usecase

import javax.inject.Inject

class CheckIfAdminCodeIsValidUseCase @Inject constructor() {

    fun execute(adminCode: String): Boolean {
        return adminCode == ADMIN_CODE
    }

    companion object {
        private const val ADMIN_CODE = "j34arw"
    }
}