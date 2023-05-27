package com.example.emafoods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.domain.usecase.GetUserGameDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authService: AuthService,
    private val getUserGameDetailsUseCase: GetUserGameDetailsUseCase,
): ViewModel() {

    private val _state = MutableStateFlow<MainViewState>(MainViewState())
    val state: StateFlow<MainViewState> = _state

    init {
        checkUserState()
    }

    private fun checkUserState() {
        viewModelScope.launch {
            val userLevel = getUserGameDetailsUseCase.execute().userLevel
            val isUserSignedIn = authService.isUserSignedIn()
            val userSignInState = if (isUserSignedIn) {
                UserSignInState.SIGNED_IN
            } else {
                UserSignInState.NOT_SIGNED_IN
            }
            _state.value = _state.value.copy(
                userLevel = userLevel,
                userSignInState = userSignInState
            )
        }
    }
}

data class MainViewState(
    val userSignInState: UserSignInState = UserSignInState.LOADING,
    val userLevel: UserLevel = UserLevel.LEVEL_1
)

enum class UserSignInState {
    SIGNED_IN,
    NOT_SIGNED_IN,
    LOADING
}