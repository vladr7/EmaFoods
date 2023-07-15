package com.example.emafoods.feature.signin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.constants.AnalyticsConstants
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.feature.game.domain.usecase.RefreshUserGameDetailsUseCase
import com.example.emafoods.feature.signin.domain.usecase.AddUserDataToRemoteDatabaseUseCase
import com.example.emafoods.feature.signin.domain.usecase.SignInAnonymousUseCase
import com.example.emafoods.feature.signin.domain.usecase.SignInGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInGoogleUseCase: SignInGoogleUseCase,
    private val signInAnonymousUseCase: SignInAnonymousUseCase,
    private val addUserDataToRemoteDatabaseUseCase: AddUserDataToRemoteDatabaseUseCase,
    private val refreshUserGameDetailsUseCase: RefreshUserGameDetailsUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val logHelper: LogHelper
) : ViewModel() {

    private val _state = MutableStateFlow<LoginViewState>(LoginViewState())
    val state: StateFlow<LoginViewState> = _state

    fun signInGoogle(idToken: String) {
        if (idToken.isEmpty()) return
        viewModelScope.launch {
            when (signInGoogleUseCase.execute(idToken)) {
                is State.Failed -> {
                    _state.update {
                        it.copy(
                            signInSuccess = false
                        )
                    }
                    logHelper.log(AnalyticsConstants.SIGN_IN_GOOGLE_FAILED)
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
                    logHelper.log(AnalyticsConstants.SIGN_IN_GOOGLE_SUCCESS)
                }
            }
        }
    }

    fun bypassSignInOnDebug() {
        _state.update {
            it.copy(
                signInSuccess = true,
                isAdmin = true
            )
        }
    }

    fun updateSignInGoogleLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                signInGoogleLoading = isLoading
            )
        }
    }

    fun updateSignInAnonymousLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                signInAnonymousLoading = isLoading
            )
        }
    }

    fun onSignInAnonymous() {
        viewModelScope.launch {
            when(val result = signInAnonymousUseCase.execute()) {
                is State.Failed -> {
                    _state.update {
                        it.copy(
                            signInSuccess = false,
                            signInAnonymousLoading = false,
                            showSignInFailedToast = true
                        )
                    }
                    logHelper.log(AnalyticsConstants.SIGN_IN_ANONYMOUS_FAILED)
                }
                is State.Success -> {
                    val userData = getUserDetailsUseCase.execute()
                    addUserDataToRemoteDatabaseUseCase.execute(userData)
                    _state.update {
                        it.copy(
                            signInSuccess = true,
                            isAdmin = false,
                            signInAnonymousLoading = false
                        )
                    }
                    logHelper.log(AnalyticsConstants.SIGN_IN_ANONYMOUS_SUCCESS)
                }
            }
        }
    }

    fun updateSignInFailedToast(showToast: Boolean) {
        _state.update {
            it.copy(
                showSignInFailedToast = showToast
            )
        }
    }
}

data class LoginViewState(
    val signInSuccess: Boolean = false,
    val isAdmin: Boolean = false,
    val signInGoogleLoading: Boolean = false,
    val signInAnonymousLoading: Boolean = false,
    val showSignInFailedToast: Boolean = false
)
