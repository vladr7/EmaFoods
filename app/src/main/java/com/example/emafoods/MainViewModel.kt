package com.example.emafoods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.feature.game.domain.usecase.RefreshUserGameDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authService: AuthService,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val refreshUserGameDetailsUseCase: RefreshUserGameDetailsUseCase
): ViewModel() {

    private val _state = MutableStateFlow<MainViewState>(MainViewState())
    val state: StateFlow<MainViewState> = _state

    init {
        checkUserState()
    }

    private fun checkUserState() {
        viewModelScope.launch {
            val isUserSignedIn = authService.isUserSignedIn()
             if (isUserSignedIn) {
                launch {
                    refreshUserGameDetailsUseCase.execute()
                }
                val userDetails = getUserDetailsUseCase.execute()
                 _state.value = _state.value.copy(
                     userSignInState = UserSignInState.SIGNED_IN,
                     isAdmin = userDetails.admin
                 )
            } else {
                 _state.value = _state.value.copy(
                     userSignInState = UserSignInState.NOT_SIGNED_IN,
                 )
            }
        }
    }
}

data class MainViewState(
    val userSignInState: UserSignInState = UserSignInState.LOADING,
    val isAdmin: Boolean = false
)

enum class UserSignInState {
    SIGNED_IN,
    NOT_SIGNED_IN,
    LOADING
}