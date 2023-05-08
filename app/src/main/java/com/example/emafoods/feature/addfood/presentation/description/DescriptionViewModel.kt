package com.example.emafoods.feature.addfood.presentation.description

import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.core.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DescriptionViewModel @Inject constructor(

) : BaseViewModel() {

    private val _state = MutableStateFlow<DescriptionViewState>(
        DescriptionViewState()
    )
    val state: StateFlow<DescriptionViewState> = _state

    fun onDescriptionChange(description: String) {
        _state.value = _state.value.copy(description = description)
    }
}

data class DescriptionViewState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val description: String = "",
) : ViewState(isLoading, errorMessage)