package com.example.emafoods.feature.addfood.presentation.ingredients

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
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
        _state.update {
            if (it.ingredientsList.contains(ingredient)) {
                it.copy(
                    showIngredientAlreadyAddedError = true
                )
            } else {
                it.copy(
                    ingredientsList = it.ingredientsList + ingredient.copy(
                        id = getNextIngredientId()
                    )
                )
            }
        }
    }

    fun removeIngredientFromList(ingredient: IngredientViewData) {
        _state.update {
            it.copy(
                ingredientsList = it.ingredientsList - ingredient
            )
        }
    }

    fun saveChangesIngredient(ingredient: IngredientViewData) {
        val newList = _state.value.ingredientsList.map { currentIngredient ->
            if (currentIngredient.id == ingredient.id) {
                currentIngredient.copy(
                    name = ingredient.name,
                    measurement = ingredient.measurement
                )
            } else {
                currentIngredient
            }
        }
        _state.value = _state.value.copy(
            ingredientsList = newList
        )
    }

    fun onShowedIngredientAlreadyAdded() {
        _state.update {
            it.copy(
                showIngredientAlreadyAddedError = false
            )
        }
    }

    private fun getNextIngredientId(): Long {
        return _state.value.ingredientsList.maxOfOrNull { it.id }?.plus(1) ?: 1
    }
}

data class IngredientsViewState(
    val ingredientsList: List<IngredientViewData> = listOf(
//        IngredientViewData(1, "Ingredient 1", 1),
//        IngredientViewData(2, "Ingredient 2", 2),
//        IngredientViewData(3, "Ingredient 3", 3),
    ),
    val categoryType: CategoryType = CategoryType.MAIN_DISH,
    val uriId: String = "",
    val showIngredientAlreadyAddedError: Boolean = false,
)