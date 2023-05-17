package com.example.emafoods.core.domain.network

import com.example.emafoods.core.domain.models.UserData

interface FirebaseService {

    fun isUserSignedIn(): Boolean
    fun getUserDetails(): UserData
}