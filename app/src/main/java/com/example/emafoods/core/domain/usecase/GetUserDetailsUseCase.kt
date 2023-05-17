package com.example.emafoods.core.domain.usecase

import com.example.emafoods.core.domain.models.UserData
import com.example.emafoods.core.domain.models.UserType
import com.example.emafoods.core.domain.network.FirebaseService
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(
    private val firebaseService: FirebaseService
) {
    fun execute(): UserData {
        val user = firebaseService.getUserDetails()
        return UserData(
            email = user.email,
            displayName = user.displayName,
            userType = if(user.email in privilegedUsers) {
                UserType.ADMIN
            } else {
                UserType.BASIC
            }
        )
    }

    private val privilegedUsers = listOf(
//        "ricean.vlad7@gmail.com",
//        "emanuela.ilie99@e-uvt.ro"
    ""
    )
}