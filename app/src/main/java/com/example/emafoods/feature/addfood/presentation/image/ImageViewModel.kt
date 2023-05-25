package com.example.emafoods.feature.addfood.presentation.image

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.core.presentation.base.ViewState
import com.example.emafoods.feature.addfood.domain.usecase.AddPendingImageToTemporaryRemoteStorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddImageViewModel @Inject constructor(
    private val addPendingImageToTemporaryRemoteStorageUseCase: AddPendingImageToTemporaryRemoteStorageUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow<ImageViewState>(ImageViewState())
    val state: StateFlow<ImageViewState> = _state

    fun updateTakePictureImageUri(imageUri: String) {
        _state.update {
            it.copy(takePictureUri = imageUri)
        }
    }

    fun updateHasTakePictureImage(hasImage: Boolean) {
        _state.update {
            it.copy(hasTakePictureImage = hasImage)
        }
    }

    fun addPendingImageToTemporarilySavedImages(imageUri: Uri) {
        viewModelScope.launch {
            addPendingImageToTemporaryRemoteStorageUseCase.execute(
                food = Food(
                    imageRef = imageUri.toString()
                )
            )
        }
        _state.update {
            it.copy(hasChooseFilesImage = true)
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
    val hasTakePictureImage: Boolean = false,
    val hasChooseFilesImage: Boolean = false,
    val takePictureUri: String = "",
) : ViewState(isLoading, errorMessage)