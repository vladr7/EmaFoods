package com.example.emafoods.feature.allfoods.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.usecase.GetAllFoodsUseCase
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.core.presentation.models.FoodMapper
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.domain.models.IngredientResult
import com.example.emafoods.feature.addfood.domain.usecase.AddIngredientToListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.RemoveIngredientFromListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.SaveChangedIngredientFromListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.UpdateIngredientFocusUseCase
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import com.example.emafoods.feature.allfoods.domain.usecase.UpdateFoodUseCase
import com.example.emafoods.feature.allfoods.presentation.models.FilterCategoryType
import com.example.emafoods.feature.allfoods.presentation.models.toFilterCategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.Normalizer
import javax.inject.Inject

@HiltViewModel
class AllFoodsViewModel @Inject constructor(
    private val getAllFoodsUseCase: GetAllFoodsUseCase,
    private val foodMapper: FoodMapper,
    private val updateIngredientFocusUseCase: UpdateIngredientFocusUseCase,
    private val addIngredientToListUseCase: AddIngredientToListUseCase,
    private val removeIngredientFromListUseCase: RemoveIngredientFromListUseCase,
    private val saveChangedIngredientFromListUseCase: SaveChangedIngredientFromListUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val updateFoodUseCase: UpdateFoodUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<AllFoodsState> = MutableStateFlow(AllFoodsState())
    val state get() = _state.asStateFlow()

    private var persistedFoods: List<FoodViewData> = emptyList()
    private var currentSelectedFoodForEditing: FoodViewData? = null

    init {
        viewModelScope.launch {
            val userDetails = getUserDetailsUseCase.execute()
            _state.update {
                it.copy(
                    isAdmin = userDetails.admin
                )
            }
        }
    }

    fun getAllFoods() {
        viewModelScope.launch {
            val foods = getAllFoodsUseCase.execute().first()
            val mappedFoods = foods.map { food ->
                foodMapper.mapToViewData(food)
            }
            persistedFoods = mappedFoods.shuffled()
            _state.update {
                it.copy(
                    foods = persistedFoods.filterFoods(it.searchText, it.filterCategoryType)
                )
            }
        }
    }

    fun onSearchTextChange(searchText: String) {
        _state.update {
            it.copy(
                searchText = searchText,
                foods = persistedFoods.filterFoods(
                    searchText = searchText,
                    filterCategoryType = it.filterCategoryType
                )
            )
        }
    }

    private fun removeDiacritics(str: String): String {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}".toRegex(), "")
    }

    private fun List<FoodViewData>.filterFoods(
        searchText: String,
        filterCategoryType: FilterCategoryType
    ): List<FoodViewData> {
        val normalizedSearchText = removeDiacritics(searchText)
        return this.filter { food ->
            (removeDiacritics(food.title).contains(normalizedSearchText, ignoreCase = true) ||
                    removeDiacritics(food.description).contains(
                        normalizedSearchText,
                        ignoreCase = true
                    )) &&
                    (filterCategoryType == FilterCategoryType.ALL ||
                            food.categoryType.toFilterCategoryType() == filterCategoryType)
        }
    }

    fun onDropDownItemClick(filterCategoryType: FilterCategoryType) {
        _state.update {
            it.copy(
                filterCategoryType = filterCategoryType,
                foods = persistedFoods.filterFoods(it.searchText, filterCategoryType)
            )
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

    fun onEditIngredients(food: FoodViewData) {
        currentSelectedFoodForEditing = food
        _state.update {
            it.copy(
                ingredientsList = food.ingredients,
                showEditIngredientsContent = true
            )
        }
    }

    fun onFinishedEditingIngredients() {
        val updatedFood = currentSelectedFoodForEditing?.copy(
            ingredients = _state.value.ingredientsList
        )
        viewModelScope.launch {
            updateFoodUseCase.execute(foodMapper.mapToModel(updatedFood ?: return@launch))
        }
        val newFoods = _state.value.foods.map { food ->
            if (food.id == updatedFood?.id) {
                updatedFood
            } else {
                food
            }
        }
        _state.update {
            it.copy(
                foods = newFoods,
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

    fun onDescriptionChanged(newDescription: String, food: FoodViewData) {
        currentSelectedFoodForEditing = food
        val updatedFood = food.copy(
            description = newDescription
        )
        _state.update {
            it.copy(
                foods = _state.value.foods.map { food ->
                    if (food.id == updatedFood.id) {
                        updatedFood
                    } else {
                        food
                    }
                }
            )
        }
    }

    fun onCancelDescriptionEditClick() {
        val persistedDescription = persistedFoods.firstOrNull { food ->
            food.id == currentSelectedFoodForEditing?.id
        }?.description ?: return
        val persistedFood = currentSelectedFoodForEditing?.copy(
            description = persistedDescription
        )
        _state.update {
            it.copy(
                foods = _state.value.foods.map { food ->
                    if (food.id == persistedFood?.id) {
                        persistedFood
                    } else {
                        food
                    }
                }
            )
        }
    }

    fun onSaveChangesDescriptionClick() {
        val updatedFood = currentSelectedFoodForEditing?.copy(
            description = _state.value.foods.first { food ->
                food.id == currentSelectedFoodForEditing?.id
            }.description
        )
        viewModelScope.launch {
            updateFoodUseCase.execute(foodMapper.mapToModel(updatedFood ?: return@launch))
        }
    }
}

data class AllFoodsState(
    val foods: List<FoodViewData> = emptyList(),
    val searchText: String = "",
    val filterCategoryType: FilterCategoryType = FilterCategoryType.ALL,
    val showEditIngredientsContent: Boolean = false,
    val showIngredientAlreadyAddedError: Boolean = false,
    val ingredientsList: List<IngredientViewData> = emptyList(),
    val isAdmin: Boolean = false,
)