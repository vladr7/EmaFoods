package com.example.emafoods.feature.addfood.presentation.congratulation

import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.constants.AnalyticsConstants
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.core.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CongratulationViewModel @Inject constructor(
    private val logHelper: LogHelper
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

    fun onInsertNewFoodClick() {
        viewModelScope.launch {
            logHelper.log(AnalyticsConstants.CLICKED_ON_ADD_NEW_RECIPE_FROM_CONGRATULATION)
        }
    }
}

data class CongratulationViewState(
    val showMessages: Boolean = false,
)