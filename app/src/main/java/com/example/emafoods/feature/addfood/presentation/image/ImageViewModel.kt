package com.example.emafoods.feature.addfood.presentation.image

import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.core.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddImageViewModel @Inject constructor(

): BaseViewModel() {

    private val _state = MutableStateFlow<ImageViewState>(ImageViewState())
    val state: StateFlow<ImageViewState> = _state

    fun updateImageUri(imageUri: String) {
        _state.update {
            it.copy(imageUri = imageUri)
        }
    }

    fun updateHasImage(hasImage: Boolean) {
        _state.update {
            it.copy(hasImage = hasImage)
        }
    }

    override fun showError(errorMessage: String) {
        _state.update {
            it.copy(errorMessage = errorMessage, isLoading = false)
        }
    }

    override fun hideError() {
        _state.update {
            it.copy(errorMessage = null, isLoading = false)
        }
    }

    override fun showLoading() {
        _state.update {
            it.copy(isLoading = true)
        }
    }

    override fun hideLoading() {
        _state.update {
            it.copy(isLoading = false)
        }
    }
}

data class ImageViewState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val hasImage: Boolean = false,
    val imageUri: String = "",
) : ViewState(isLoading, errorMessage)