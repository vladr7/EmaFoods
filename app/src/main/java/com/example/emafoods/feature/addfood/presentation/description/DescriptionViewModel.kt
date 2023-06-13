package com.example.emafoods.feature.addfood.presentation.description

import androidx.lifecycle.SavedStateHandle
import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.core.presentation.base.ViewState
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import com.example.emafoods.feature.addfood.presentation.description.navigation.DescriptionArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DescriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stringDecoder: StringDecoder,
    ) : BaseViewModel() {

    private val descriptionArgs: DescriptionArguments =
        DescriptionArguments(savedStateHandle, stringDecoder)
    private val uriId = descriptionArgs.uri
    private val categoryId = descriptionArgs.category
    private val ingredientsList = descriptionArgs.ingredientsList

    private val _state = MutableStateFlow<DescriptionViewState>(
        DescriptionViewState()
    )
    val state: StateFlow<DescriptionViewState> = _state

    init {
        _state.update {
            it.copy(
                uri = uriId,
                category = categoryId,
                ingredientsListSerialized = ingredientsList
            )
        }
    }

    fun onDescriptionChange(description: String) {
        if (description.length > 10) {
            _state.value = _state.value.copy(showNextButton = true, description = description)
        } else {
            _state.value = _state.value.copy(showNextButton = false, description = description)
        }
    }
}

data class DescriptionViewState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val description: String = "",
    val showNextButton: Boolean = false,
    val uri: String = "",
    val category: String = "",
    val ingredientsListSerialized: String = "",
) : ViewState(isLoading, errorMessage)