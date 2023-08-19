package com.example.emafoods.feature.allfoods.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.usecase.GetAllFoodsUseCase
import com.example.emafoods.core.presentation.models.FoodMapper
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.allfoods.presentation.models.FilterCategoryType
import com.example.emafoods.feature.allfoods.presentation.models.toFilterCategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.Normalizer
import javax.inject.Inject

@HiltViewModel
class AllFoodsViewModel @Inject constructor(
    private val getAllFoodsUseCase: GetAllFoodsUseCase,
    private val foodMapper: FoodMapper,
) : ViewModel() {

    private val _state: MutableStateFlow<AllFoodsState> = MutableStateFlow(AllFoodsState())
    val state get() = _state.asStateFlow()

    private var persistedFoods: List<FoodViewData> = emptyList()

    init {
        getAllFoods()
    }

    private fun getAllFoods() {
        viewModelScope.launch {
            getAllFoodsUseCase.execute().collectLatest { foods ->
                val mappedFoods = foods.map { food ->
                    foodMapper.mapToViewData(food)
                }
                persistedFoods = mappedFoods.shuffled()
                _state.update {
                    it.copy(
                        foods = mappedFoods.filterFoods(it.searchText, it.filterCategoryType)
                    )
                }
            }
        }
    }

    fun onSearchTextChange(searchText: String) {
        _state.update {
            it.copy(
                searchText = searchText,
                foods = persistedFoods.filterFoods(searchText = searchText, filterCategoryType = it.filterCategoryType)
                    .filter { food ->
                        food.categoryType.toFilterCategoryType() == it.filterCategoryType
                    }
            )
        }
    }

    private fun removeDiacritics(str: String): String {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}".toRegex(), "")
    }

    private fun List<FoodViewData>.filterFoods(searchText: String, filterCategoryType: FilterCategoryType): List<FoodViewData> {
        val normalizedSearchText = removeDiacritics(searchText)
        return this.filter { food ->
            (removeDiacritics(food.title).contains(normalizedSearchText, ignoreCase = true) ||
                    removeDiacritics(food.description).contains(normalizedSearchText, ignoreCase = true)) &&
                    (filterCategoryType == FilterCategoryType.ALL ||
                            food.categoryType.toFilterCategoryType() == filterCategoryType)
        }
    }

    fun onDropDownItemClick(filterCategoryType: FilterCategoryType) {
        val filteredFoods = persistedFoods.filterFoods(_state.value.searchText, filterCategoryType)
        println("vlad: filteredFoods: $filteredFoods")
        _state.update {
            it.copy(
                filterCategoryType = filterCategoryType,
                foods = persistedFoods.filterFoods(it.searchText, filterCategoryType)
            )
        }
    }
}

data class AllFoodsState(
    val foods: List<FoodViewData> = emptyList(),
    val searchText: String = "",
    val filterCategoryType: FilterCategoryType = FilterCategoryType.ALL,
)