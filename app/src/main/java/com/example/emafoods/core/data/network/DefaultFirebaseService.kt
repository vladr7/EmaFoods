package com.example.emafoods.core.data.network

import com.example.emafoods.core.domain.models.UserData
import com.example.emafoods.core.domain.network.FirebaseService
import com.google.firebase.auth.FirebaseAuth

class DefaultFirebaseService : FirebaseService {

    override fun isUserSignedIn(): Boolean =
        FirebaseAuth.getInstance().currentUser != null

    override fun getUserDetails(): UserData =
        UserData(
            FirebaseAuth.getInstance().currentUser?.email ?: "",
            FirebaseAuth.getInstance().currentUser?.displayName ?: "",
        )
}