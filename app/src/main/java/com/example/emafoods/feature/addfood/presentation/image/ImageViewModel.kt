package com.example.emafoods.feature.addfood.presentation.image

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.extension.getCompressedImage
import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.core.presentation.base.ViewState
import com.example.emafoods.feature.addfood.domain.usecase.AddTemporaryPendingImageToRemoteStorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddImageViewModel @Inject constructor(
    private val addTemporaryPendingImageToRemoteStorageUseCase: AddTemporaryPendingImageToRemoteStorageUseCase,
) : BaseViewModel() {

    private val _state = MutableStateFlow<ImageViewState>(ImageViewState())
    val state: StateFlow<ImageViewState> = _state

    fun updateImageUri(imageUri: String) {
        _state.update {
            it.copy(imageUri = imageUri)
        }
    }

    fun updateHasTakePictureImage(hasImage: Boolean) {
        _state.update {
            it.copy(
                hasTakePictureImage = hasImage,
                hasChooseFilesImage = false
            )
        }
    }

    fun addPendingImageToTemporarilySavedImages(imageUri: Uri, context: Context) {
        viewModelScope.launch {
            val compressedUri = imageUri.getCompressedImage(context)
            addTemporaryPendingImageToRemoteStorageUseCase.execute(
                food = Food(
                    imageRef = compressedUri.toString()
                )
            )
        }
        _state.update {
            it.copy(
                hasChooseFilesImage = true,
                hasTakePictureImage = false
            )
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
    val imageUri: String = Uri.EMPTY.toString(),
) : ViewState(isLoading, errorMessage)