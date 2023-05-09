package com.example.emafoods.feature.addfood.presentation.insert

import android.net.Uri
import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.core.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class InsertFoodViewModel @Inject constructor(

) : BaseViewModel() {

    private val _state = MutableStateFlow<InsertFoodViewState>(
        InsertFoodViewState()
    )
    val state: StateFlow<InsertFoodViewState> = _state

    fun updateTitle(title: String) {
        if (title.length > 4) {
            _state.value = _state.value.copy(showNextButton = true, title = title)
        } else {
            _state.value = _state.value.copy(showNextButton = false, title = title)
        }
    }

    fun updateDescription(description: String) {
        _state.value = _state.value.copy(description = description)
    }
}

data class InsertFoodViewState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val imageUri: Uri? = null,
    val title: String = "",
    val description: String = "",
    val showNextButton: Boolean = false,
) : ViewState(isLoading, errorMessage)
