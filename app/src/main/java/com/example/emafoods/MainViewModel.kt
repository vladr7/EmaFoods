package com.example.emafoods

import androidx.lifecycle.ViewModel
import com.example.emafoods.core.domain.network.FirebaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseService: FirebaseService
): ViewModel() {

    private val _state = MutableStateFlow<MainViewState>(MainViewState())
    val state: StateFlow<MainViewState> = _state

    init {
        checkIfUserIsSignedIn()
    }

    private fun checkIfUserIsSignedIn() {
        _state.value = _state.value.copy(
            isUserSignedIn = firebaseService.isUserSignedIn()
        )
    }
}

data class MainViewState(
    val isUserSignedIn: Boolean = false,
)