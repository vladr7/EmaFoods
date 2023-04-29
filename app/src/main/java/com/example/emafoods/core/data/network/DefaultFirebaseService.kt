package com.example.emafoods.core.data.network

import com.example.emafoods.core.domain.network.FirebaseService
import com.google.firebase.auth.FirebaseAuth

class DefaultFirebaseService : FirebaseService {

    override fun isUserSignedIn(): Boolean =
        FirebaseAuth.getInstance().currentUser != null

}