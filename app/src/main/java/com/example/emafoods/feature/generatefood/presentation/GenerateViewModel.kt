package com.example.emafoods.feature.generatefood.presentation

import androidx.lifecycle.ViewModel
import com.example.emafoods.core.presentation.models.FoodMapper
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.generatefood.domain.usecase.GenerateFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val foodMapper: FoodMapper,
    private val generateFoodUseCase: GenerateFoodUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<GenerateViewState>(GenerateViewState())
    val state: StateFlow<GenerateViewState> = _state

    fun generateFoodEvent() {
        val food = generateFoodUseCase.execute()
        _state.update {
            it.copy(
                food = foodMapper.mapToViewData(food)
            )
        }
    }
}

data class GenerateViewState(
    val food: FoodViewData = FoodViewData(),
    val isNetworkAvailable: Boolean? = null,
)