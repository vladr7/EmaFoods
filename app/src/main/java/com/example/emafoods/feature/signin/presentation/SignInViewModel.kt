package com.example.emafoods.feature.signin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.feature.game.domain.usecase.RefreshUserGameDetailsUseCase
import com.example.emafoods.feature.signin.domain.usecase.AddUserDataToRemoteDatabaseUseCase
import com.example.emafoods.feature.signin.domain.usecase.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val addUserDataToRemoteDatabaseUseCase: AddUserDataToRemoteDatabaseUseCase,
    private val refreshUserGameDetailsUseCase: RefreshUserGameDetailsUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LoginViewState>(LoginViewState())
    val state: StateFlow<LoginViewState> = _state

    fun signIn(idToken: String) {
        if (idToken.isEmpty()) return
        viewModelScope.launch {
            when (signInUseCase.execute(idToken)) {
                is State.Failed -> {
                    _state.update {
                        it.copy(
                            signInSuccess = false
                        )
                    }
                }
                is State.Success -> {
                    refreshUserGameDetailsUseCase.execute()
                    val userData = getUserDetailsUseCase.execute()
                    addUserDataToRemoteDatabaseUseCase.execute(userData)
                    _state.update {
                        it.copy(
                            signInSuccess = true,
                            isAdmin = userData.admin
                        )
                    }
                }
            }
        }
    }

    fun updateSignInLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                signInLoading = isLoading
            )
        }
    }
}

data class LoginViewState(
    val signInSuccess: Boolean = false,
    val isAdmin: Boolean = false,
    val signInLoading: Boolean = false
)
