package com.example.emafoods.feature.generatefood.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.usecase.RefreshFoodsUseCase
import com.example.emafoods.core.presentation.models.FoodMapper
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.generatefood.domain.usecase.GenerateFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val foodMapper: FoodMapper,
    private val generateFoodUseCase: GenerateFoodUseCase,
    private val refreshFoodsUseCase: RefreshFoodsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<GenerateViewState>(GenerateViewState())
    val state: StateFlow<GenerateViewState> = _state

    init {
        refreshFoodsFromRepository()
    }

    private fun refreshFoodsFromRepository() {
        viewModelScope.launch {
            refreshFoodsUseCase.execute()
        }
    }

    fun generateFoodEvent() {
        viewModelScope.launch {
            val food = generateFoodUseCase.execute()
            _state.update {
                it.copy(
                    food = foodMapper.mapToViewData(food)
                )
            }
        }
    }
}

data class GenerateViewState(
    val food: FoodViewData = FoodViewData(),
    val isNetworkAvailable: Boolean? = null,
    val foodHasBeenGenerated: Boolean = false
)