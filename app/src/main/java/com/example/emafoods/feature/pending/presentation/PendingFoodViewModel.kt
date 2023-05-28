package com.example.emafoods.feature.pending.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.usecase.RefreshFoodsUseCase
import com.example.emafoods.core.domain.usecase.RefreshPendingFoodsUseCase
import com.example.emafoods.core.presentation.models.FoodMapper
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.game.domain.usecase.AddRewardToUserAcceptedRecipeUseCase
import com.example.emafoods.feature.pending.domain.usecase.DeletePendingFoodUseCase
import com.example.emafoods.feature.pending.domain.usecase.GetAllPendingFoodsUseCase
import com.example.emafoods.feature.pending.domain.usecase.MovePendingFoodToAllFoodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
//    private val getFilteredPendingFoodsUseCase: GetFilteredPendingFoodsUseCase, // todo uncomment
    private val deletePendingFoodUseCase: DeletePendingFoodUseCase,
    private val refreshFoodsUseCase: RefreshFoodsUseCase,
    private val movePendingFoodToAllFoodsUseCase: MovePendingFoodToAllFoodsUseCase,
    private val foodMapper: FoodMapper,
    private val addRewardToUserAcceptedRecipeUseCase: AddRewardToUserAcceptedRecipeUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<PendingFoodState> = MutableStateFlow(PendingFoodState())
    val state get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            refreshPendingFoodsUseCase.execute()
        }
        viewModelScope.launch {
            fetchPendingFoods()
        }
    }

    private suspend fun fetchPendingFoods() {
        getAllPendingFoodsUseCase.execute().collectLatest { foods ->
            if (foods.isNotEmpty()) {
                _state.value = _state.value.copy(
                    pendingFoods = foods.map { foodMapper.mapToViewData(it) },
                    currentFood = foodMapper.mapToViewData(foods.first())
                )
            } else {
                _state.value = _state.value.copy(
                    pendingFoods = emptyList(),
                    currentFood = FoodViewData()
                )
            }
        }
    }

    fun onSwipeLeft() {
        val currentFood = _state.value.currentFood
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = deletePendingFoodUseCase.execute(foodMapper.mapToModel(currentFood))) {
                is State.Failed -> {}

                is State.Success -> {
                    _state.update {
                        it.copy(showDeleteSuccessfully = true)
                    }
                    launch {
                        refreshPendingFoodsUseCase.execute()
                    }
                }
            }
        }
    }

    fun onSwipeRight() {
        val currentFood = _state.value.currentFood
        viewModelScope.launch(Dispatchers.IO) {
            if(state.value.pendingFoods.isNotEmpty()) {
                _state.update {
                    it.copy(showMovedSuccessfully = true)
                }
            }
            when (val result =
                movePendingFoodToAllFoodsUseCase.execute(foodMapper.mapToModel(currentFood))) {
                is State.Failed -> {}

                is State.Success -> {
                    addRewardToUserAcceptedRecipeUseCase.execute(foodMapper.mapToModel(currentFood))
                    launch {
                        refreshPendingFoodsUseCase.execute()
                    }
                    launch {
                        refreshFoodsUseCase.execute()
                    }
                }
            }
        }
    }

    fun onResetMessageStates() {
        _state.update {
            it.copy(
                error = "",
                showError = false,
                showMovedSuccessfully = false,
                showDeleteSuccessfully = false
            )
        }
    }
}

data class PendingFoodState(
    val pendingFoods: List<FoodViewData> = emptyList(),
    val isLoading: Boolean = false,
    val showError: Boolean = false,
    val error: String = "",
    val currentFood: FoodViewData = FoodViewData(),
    val showMovedSuccessfully: Boolean = false,
    val showDeleteSuccessfully: Boolean = false
)