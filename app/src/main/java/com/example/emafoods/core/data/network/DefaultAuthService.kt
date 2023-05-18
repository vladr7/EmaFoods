package com.example.emafoods.core.data.network

import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.models.UserData
import com.example.emafoods.core.domain.network.AuthService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultAuthService @Inject constructor(

) : AuthService {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun isUserSignedIn(): Boolean =
        firebaseAuth.currentUser != null

    override fun getUserDetails(): UserData =
        UserData(
            firebaseAuth.currentUser?.email ?: "",
            firebaseAuth.currentUser?.displayName ?: "",
        )

    override fun signOut() {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(idToken: String): State<Unit> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        continuation.resume(State.success(Unit))
                    } else {
                        continuation.resume(State.failed(task.exception?.localizedMessage ?: ""))
                    }
                }
        }
    }
}