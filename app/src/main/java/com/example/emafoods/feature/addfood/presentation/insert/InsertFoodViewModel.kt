package com.example.emafoods.feature.addfood.presentation.insert

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.usecase.RefreshPendingFoodsUseCase
import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.core.presentation.base.ViewState
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import com.example.emafoods.feature.addfood.domain.usecase.GetTemporaryPendingImageUseCase
import com.example.emafoods.feature.addfood.domain.usecase.InsertFoodUseCase
import com.example.emafoods.feature.addfood.presentation.insert.navigation.InsertFoodArguments
import com.example.emafoods.feature.game.domain.usecase.IncreaseXpUseCase
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsertFoodViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stringDecoder: StringDecoder,
    private val insertFoodUseCase: InsertFoodUseCase,
    private val refreshPendingFoodsUseCase: RefreshPendingFoodsUseCase,
    private val increaseXpUseCase: IncreaseXpUseCase,
    private val getTemporaryPendingImageUseCase: GetTemporaryPendingImageUseCase
) : BaseViewModel() {

    private val insertFoodArgs: InsertFoodArguments =
        InsertFoodArguments(savedStateHandle, stringDecoder)
    private val uriId = insertFoodArgs.uri
    private val descriptionId = insertFoodArgs.description

    private val _state = MutableStateFlow<InsertFoodViewState>(
        InsertFoodViewState()
    )
    val state: StateFlow<InsertFoodViewState> = _state

    init {
        if(uriId == "empty") {
            viewModelScope.launch {
                when(val result = getTemporaryPendingImageUseCase.execute()) {
                    is State.Failed -> {
                        _state.update {
                            it.copy(errorMessage = result.message, description = descriptionId)
                        }
                    }
                    is State.Success -> {
                        _state.update {
                            it.copy(shouldAddImageFromTemporary = true, imageUri = result.data, description = descriptionId)
                        }
                    }
                }
            }
        } else {
            _state.update {
                it.copy(imageUri = Uri.parse(uriId), description = descriptionId)
            }
        }
    }

    fun updateDescription(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    fun updateImageUri(uri: Uri?) {
        if (uri != null) {
            _state.value = _state.value.copy(imageUri = uri)
        } else {
            _state.update {
                it.copy(errorMessage = "Te rog adauga o imagine a retetei")
            }
        }
    }

    fun insertFood(
        description: String,
        imageUri: Uri?
    ) {
        if (state.value.isLoading) {
            return
        }
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            when (val result = insertFoodUseCase.execute(
                food = Food(
                    description = description,
                ),
                fileUri = imageUri ?: Uri.EMPTY,
                shouldAddImageFromTemporary = state.value.shouldAddImageFromTemporary
            )) {
                is State.Failed -> _state.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }

                is State.Success -> {
                    _state.update {
                        it.copy(isLoading = false, errorMessage = null, insertFoodSuccess = true)
                    }
                    refreshPendingFoodsUseCase.execute()
                }
            }
        }
    }

    fun resetState() {
        _state.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                imageUri = null,
                description = "",
                insertFoodSuccess = false,
            )
        }
    }

    override fun hideError() {
        _state.update {
            it.copy(errorMessage = null)
        }
    }

    override fun onXpIncrease() {
        viewModelScope.launch {
            increaseXpUseCase.execute(IncreaseXpActionType.ADD_RECIPE)
        }
    }

    fun onSelectedNewImage() {
        _state.update {
            it.copy(shouldAddImageFromTemporary = false)
        }
    }
}

data class InsertFoodViewState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val imageUri: Uri? = null,
    val description: String = "",
    val insertFoodSuccess: Boolean = false,
    val shouldAddImageFromTemporary: Boolean = false,
) : ViewState(isLoading, errorMessage)
