package com.example.emafoods.feature.addfood.presentation.insert

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.core.presentation.base.ViewState
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import com.example.emafoods.feature.addfood.presentation.insert.navigation.InsertFoodArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InsertFoodViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stringDecoder: StringDecoder,
) : BaseViewModel() {

    private val insertFoodArgs: InsertFoodArguments = InsertFoodArguments(savedStateHandle, stringDecoder)
    private val uriId = insertFoodArgs.uri
    private val descriptionId = insertFoodArgs.description

    private val _state = MutableStateFlow<InsertFoodViewState>(
        InsertFoodViewState()
    )
    val state: StateFlow<InsertFoodViewState> = _state

    init {
        _state.update {
            it.copy(imageUri = Uri.parse(uriId), description = descriptionId)
        }
    }

    fun updateDescription(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    fun updateImageUri(uri: Uri?) {
        if(uri != null) {
            _state.value = _state.value.copy(imageUri = uri)
        } else {
            _state.update {
                it.copy(errorMessage = "Please select an image")
            }
        }
    }
}

data class InsertFoodViewState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val imageUri: Uri? = null,
    val description: String = "",
    val showNextButton: Boolean = false,
) : ViewState(isLoading, errorMessage)
