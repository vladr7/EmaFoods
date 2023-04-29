package com.example.emafoods.feature.signin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(

) : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow<LoginViewState>(LoginViewState())
    val state: StateFlow<LoginViewState> = _state

    fun signIn(email: String, displayName: String, idToken: String) {
        if (idToken.isEmpty()) return

        firebaseAuthWithGoogle(idToken)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    println("SignIn success! ${task.result.user}")
                    _state.update {
                        it.copy(
                            signInSuccess = true
                        )
                    }
                } else {
                    println("SignIn failed: ${task.exception?.localizedMessage}")
                }
            }
    }
}

data class LoginViewState(
    val user: CrashlyticsReport.Session.User? = null,
    val signInSuccess: Boolean = false,
)
