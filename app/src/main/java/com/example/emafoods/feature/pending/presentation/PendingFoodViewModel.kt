package com.example.emafoods.feature.pending.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.usecase.GetAllPendingFoodsUseCase
import com.example.emafoods.core.domain.usecase.RefreshPendingFoodsUseCase
import com.example.emafoods.core.presentation.models.FoodMapper
import com.example.emafoods.core.presentation.models.FoodViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingFoodViewModel @Inject constructor(
    private val refreshPendingFoodsUseCase: RefreshPendingFoodsUseCase,
    private val getAllPendingFoodsUseCase: GetAllPendingFoodsUseCase,
    private val foodMapper: FoodMapper
) : ViewModel() {

    fun onSwipeLeft() {
        _state.update {
            it.copy(
                food = if (it.pendingFoods.isNotEmpty()) it.pendingFoods.random() else FoodViewData()
            )
        }
    }

    fun onSwipeRight() {
        _state.update {
            it.copy(
                food = if (it.pendingFoods.isNotEmpty()) it.pendingFoods.random() else FoodViewData()
            )
        }
    }

    private val _state: MutableStateFlow<PendingFoodState> = MutableStateFlow(PendingFoodState())
    val state get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            refreshPendingFoodsUseCase.execute()
        }
        viewModelScope.launch {
            getAllPendingFoodsUseCase.execute().collectLatest { foods ->
                if (foods.isNotEmpty()) {
                    _state.value = _state.value.copy(
                        pendingFoods = foods.map { foodMapper.mapToViewData(it) },
                        food = foodMapper.mapToViewData(foods.first())
                    )
                }
            }
        }
    }
}

data class PendingFoodState(
    val pendingFoods: List<FoodViewData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val food: FoodViewData = FoodViewData(),
)