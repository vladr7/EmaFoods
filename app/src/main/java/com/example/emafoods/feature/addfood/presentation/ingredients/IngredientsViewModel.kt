package com.example.emafoods.feature.addfood.presentation.ingredients

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import com.example.emafoods.feature.addfood.domain.models.IngredientResult
import com.example.emafoods.feature.addfood.domain.usecase.AddIngredientToListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.RemoveIngredientFromListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.SaveChangedIngredientFromListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.SerializeIngredientsUseCase
import com.example.emafoods.feature.addfood.domain.usecase.UpdateIngredientFocusUseCase
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientMapper
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import com.example.emafoods.feature.addfood.presentation.ingredients.navigation.IngredientsArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class IngredientsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stringDecoder: StringDecoder,
    private val serializeIngredientsUseCase: SerializeIngredientsUseCase,
    private val ingredientsMapper: IngredientMapper,
    private val addIngredientToListUseCase: AddIngredientToListUseCase,
    private val removeIngredientFromListUseCase: RemoveIngredientFromListUseCase,
    private val saveChangedIngredientFromListUseCase: SaveChangedIngredientFromListUseCase,
    private val updateIngredientFocusUseCase: UpdateIngredientFocusUseCase
) : ViewModel() {

    private val ingredientsArgs: IngredientsArguments =
        IngredientsArguments(savedStateHandle, stringDecoder)
    private val uriId = ingredientsArgs.uri
    private val categoryId = ingredientsArgs.category

    private val _state = MutableStateFlow<IngredientsViewState>(IngredientsViewState())
    val state: StateFlow<IngredientsViewState> = _state

    init {
        _state.update {
            it.copy(
                categoryType = CategoryType.fromString(categoryId),
                uriId = uriId
            )
        }
    }

    fun addIngredientToList(ingredient: IngredientViewData) {
        when(val result = addIngredientToListUseCase.execute(ingredient, _state.value.ingredientsList)) {
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
        when(val result = removeIngredientFromListUseCase.execute(ingredient, _state.value.ingredientsList)) {
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
        when(val result = saveChangedIngredientFromListUseCase.execute(ingredient, _state.value.ingredientsList)) {
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

    fun serializedIngredients(): String {
        val mappedIngredients = state.value.ingredientsList.map {
            ingredientsMapper.mapToModel(it)
        }
        return serializeIngredientsUseCase.execute(mappedIngredients)
    }

    fun onUpdateIngredientFocus(ingredient: IngredientViewData, isFocused: Boolean) {
        when(val result = updateIngredientFocusUseCase.execute(_state.value.ingredientsList, ingredient, isFocused)) {
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
}

data class IngredientsViewState(
    val ingredientsList: List<IngredientViewData> = emptyList(),
    val categoryType: CategoryType = CategoryType.MAIN_DISH,
    val uriId: String = "",
    val showIngredientAlreadyAddedError: Boolean = false,
)