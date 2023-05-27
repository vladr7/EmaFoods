package com.example.emafoods.feature.addfood.presentation.congratulation

import com.example.emafoods.core.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CongratulationViewModel @Inject constructor(

): BaseViewModel() {

    private val _state = MutableStateFlow<CongratulationViewState>(CongratulationViewState())
    val state: StateFlow<CongratulationViewState> = _state

    init {
        _state.value = CongratulationViewState(
            showMessages = true
        )
    }

    fun onMessagesShown() {
        _state.value = CongratulationViewState(
            showMessages = false
        )
    }
}

data class CongratulationViewState(
    val showMessages: Boolean = false,
)