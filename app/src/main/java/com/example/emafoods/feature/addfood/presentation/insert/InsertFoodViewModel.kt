package com.example.emafoods.feature.addfood.presentation.insert

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.constants.AnalyticsConstants
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.core.domain.usecase.RefreshPendingFoodsUseCase
import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.core.presentation.base.ViewState
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import com.example.emafoods.feature.addfood.domain.models.IngredientResult
import com.example.emafoods.feature.addfood.domain.usecase.AddIngredientToListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.GetTemporaryPendingImageUseCase
import com.example.emafoods.feature.addfood.domain.usecase.InsertFoodUseCase
import com.example.emafoods.feature.addfood.domain.usecase.RemoveIngredientFromListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.SaveChangedIngredientFromListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.UpdateIngredientFocusUseCase
import com.example.emafoods.feature.addfood.presentation.image.navigation.IMAGE_FROM_GALLERY_FLAG
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
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
    private val getTemporaryPendingImageUseCase: GetTemporaryPendingImageUseCase,
    private val logHelper: LogHelper,
    private val addIngredientToListUseCase: AddIngredientToListUseCase,
    private val removeIngredientFromListUseCase: RemoveIngredientFromListUseCase,
    private val saveChangedIngredientFromListUseCase: SaveChangedIngredientFromListUseCase,
    private val updateIngredientFocusUseCase: UpdateIngredientFocusUseCase,
) : BaseViewModel() {

    private val insertFoodArgs: InsertFoodArguments =
        InsertFoodArguments(savedStateHandle, stringDecoder)
    private val uriId = insertFoodArgs.uri
    private val categoryId = insertFoodArgs.category
    private val titleId = insertFoodArgs.title

    private val _state = MutableStateFlow<InsertFoodViewState>(
        InsertFoodViewState()
    )
    val state: StateFlow<InsertFoodViewState> = _state

    init {
        _state.update {
            it.copy(title = titleId)
        }
        if (uriId == IMAGE_FROM_GALLERY_FLAG) {
            viewModelScope.launch {
                when (val result = getTemporaryPendingImageUseCase.execute()) {
                    is State.Failed -> {
                        _state.update {
                            it.copy(errorMessage = result.message)
                        }
                    }

                    is State.Success -> {
                        _state.update {
                            it.copy(shouldAddImageFromTemporary = true, imageUri = result.data)
                        }
                    }
                }
            }
        } else {
            _state.update {
                it.copy(imageUri = Uri.parse(uriId))
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
        imageUri: Uri?,
        ingredients: List<IngredientViewData>
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
                    category = categoryId,
                ),
                ingredients = ingredients,
                fileUri = imageUri ?: Uri.EMPTY,
                shouldAddImageFromTemporary = state.value.shouldAddImageFromTemporary
            )) {
                is State.Failed -> {
                    _state.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
                    logHelper.log(AnalyticsConstants.ADD_RECIPE_FAILED)
                    logHelper.reportCrash(Throwable("Insert food failed ${result.message}"))
                }

                is State.Success -> {
                    _state.update {
                        it.copy(isLoading = false, errorMessage = null, insertFoodSuccess = true)
                    }
                    refreshPendingFoodsUseCase.execute()
                    logHelper.log(AnalyticsConstants.ADD_RECIPE_SUCCESS)
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
        viewModelScope.launch {
            logHelper.log(AnalyticsConstants.RE_PICK_PHOTO)
        }
    }

    fun addIngredientToList(ingredient: IngredientViewData) {
        when (val result =
            addIngredientToListUseCase.execute(ingredient, _state.value.ingredientsList)) {
            is IngredientResult.ErrorAlreadyAdded -> {
                _state.update {
                    it.copy(
                        showIngredientAlreadyAddedError = true
                    )
                }
            }

            is IngredientResult.Success -> {
                _state.update {
                    it.copy(
                        ingredientsList = result.data
                    )
                }
            }
        }
    }

    fun removeIngredientFromList(ingredient: IngredientViewData) {
        when (val result =
            removeIngredientFromListUseCase.execute(ingredient, _state.value.ingredientsList)) {
            is IngredientResult.ErrorAlreadyAdded -> {}
            is IngredientResult.Success -> {
                _state.update {
                    it.copy(
                        ingredientsList = result.data
                    )
                }
            }
        }
    }

    fun saveChangesIngredient(ingredient: IngredientViewData) {
        when (val result = saveChangedIngredientFromListUseCase.execute(
            ingredient,
            _state.value.ingredientsList
        )) {
            is IngredientResult.ErrorAlreadyAdded -> {}
            is IngredientResult.Success -> {
                _state.update {
                    it.copy(
                        ingredientsList = result.data
                    )
                }
            }
        }
    }

    fun onShowedIngredientAlreadyAdded() {
        _state.update {
            it.copy(
                showIngredientAlreadyAddedError = false
            )
        }
    }

    fun onEditIngredients() {
        _state.update {
            it.copy(
                showEditIngredientsContent = true
            )
        }
    }

    fun onFinishedEditingIngredients() {
        _state.update {
            it.copy(
                showEditIngredientsContent = false
            )
        }
    }

    fun onUpdateIngredientFocus(ingredient: IngredientViewData, isFocused: Boolean) {
        when (val result = updateIngredientFocusUseCase.execute(
            _state.value.ingredientsList,
            ingredient,
            isFocused
        )) {
            is IngredientResult.ErrorAlreadyAdded -> {}
            is IngredientResult.Success -> {
                _state.update {
                    it.copy(
                        ingredientsList = result.data
                    )
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _state.update {
            it.copy(
                title = title
            )
        }
    }

    data class InsertFoodViewState(
        override val isLoading: Boolean = false,
        override val errorMessage: String? = null,
        val showEditIngredientsContent: Boolean = false,
        val showIngredientAlreadyAddedError: Boolean = false,
        val imageUri: Uri? = null,
        val description: String = "",
        val insertFoodSuccess: Boolean = false,
        val shouldAddImageFromTemporary: Boolean = false,
        val ingredientsList: List<IngredientViewData> = emptyList(),
        val title: String = ""
    ) : ViewState(isLoading, errorMessage)
}
