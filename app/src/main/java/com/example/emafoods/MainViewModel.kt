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
    private val getUserGameDetailsUseCase: GetUserGameDetailsUseCase
): ViewModel() {

    private val _state = MutableStateFlow<MainViewState>(MainViewState())
    val state: StateFlow<MainViewState> = _state

    init {
        checkIfUserIsSignedIn()
        checkUserLevel()
    }

    private fun checkUserLevel() {
        viewModelScope.launch {
            val userLevel = getUserGameDetailsUseCase.execute().userLevel
            _state.value = _state.value.copy(
                userLevel = userLevel
            )
        }
    }

    private fun checkIfUserIsSignedIn() {
        _state.value = _state.value.copy(
            isUserSignedIn = authService.isUserSignedIn()
        )
    }
}

data class MainViewState(
    val isUserSignedIn: Boolean = false,
    val userLevel: UserLevel = UserLevel.LEVEL_1
)