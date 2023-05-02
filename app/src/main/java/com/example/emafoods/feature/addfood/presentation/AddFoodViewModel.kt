package com.example.emafoods.feature.addfood.presentation

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.usecase.RefreshFoodsUseCase
import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.core.presentation.base.ViewState
import com.example.emafoods.core.utils.State
import com.example.emafoods.feature.addfood.domain.usecase.AddFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    private val addFoodUseCase: AddFoodUseCase,
    private val refreshFoodsUseCase: RefreshFoodsUseCase
): BaseViewModel() {

    private val _state = MutableStateFlow<AddFoodViewState>(AddFoodViewState())
    val state: StateFlow<AddFoodViewState> = _state

    fun addFood() {
        viewModelScope.launch {
            showLoading()
            when (val state = addFoodUseCase.execute(
                food = state.value.foodItem,
                imageUri = state.value.imageUri
            )) {
                is State.Failed -> {
                    showError(errorMessage = state.message)
                }
                is State.Loading -> {
                    showLoading()
                }
                is State.Success -> {
                    refreshFoodsUseCase.execute()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }
            }
        }
    }

    fun updateFoodTitle(title: String) {
        _state.update {
            it.copy(foodTitle = title, foodItem = it.foodItem.copy(title = title))
        }
    }

    fun updateFoodDescription(description: String) {
        _state.update {
            it.copy(
                foodDescription = description,
                foodItem = it.foodItem.copy(description = description)
            )
        }
    }

    fun updateImageUri(imageUri: Uri?) {
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

data class AddFoodViewState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val foodTitle: String = "",
    val foodDescription: String = "",
    val foodItem: Food = Food(id = UUID.randomUUID().toString()),
    val hasImage: Boolean = false,
    val imageUri: Uri? = null
) : ViewState(isLoading, errorMessage)