package com.example.emafoods.feature.generatefood.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.usecase.RefreshFoodsUseCase
import com.example.emafoods.core.presentation.common.alert.XP_INCREASE_THRESHOLD
import com.example.emafoods.core.presentation.models.FoodMapper
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.game.domain.usecase.GetUnspentUserXpUseCase
import com.example.emafoods.feature.game.domain.usecase.ResetUnspentUserXpUseCase
import com.example.emafoods.feature.game.domain.usecase.StoreUnspentUserXpUseCase
import com.example.emafoods.feature.game.domain.usecase.StoreUserXpUseCase
import com.example.emafoods.feature.game.presentation.model.IncreaseXpActionType
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
    private val refreshFoodsUseCase: RefreshFoodsUseCase,
    private val storeUnspentUserXpUseCase: StoreUnspentUserXpUseCase,
    private val getUnspentUserXpUseCase: GetUnspentUserXpUseCase,
    private val storeUserXpUseCase: StoreUserXpUseCase,
    private val resetUnspentUserXpUseCase: ResetUnspentUserXpUseCase,
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
                    food = foodMapper.mapToViewData(food),
                    foodHasBeenGenerated = true,
                )
            }
        }
    }

    fun onXpIncrease() {
        viewModelScope.launch {
            storeUnspentUserXpUseCase.execute(IncreaseXpActionType.GENERATE_RECIPE.xp)
            val unspentUserXp = getUnspentUserXpUseCase.execute()
            if(unspentUserXp >= XP_INCREASE_THRESHOLD) {
                storeUserXpUseCase.execute(unspentUserXp)
                resetUnspentUserXpUseCase.execute()
                storeUserXpUseCase.execute(unspentUserXp)
                _state.update {
                    it.copy(
                        showXpIncreaseToast = true,
                        xpIncreased = unspentUserXp
                    )
                }
            }
        }
    }

    fun onXpIncreaseToastShown() {
        _state.update {
            it.copy(
                showXpIncreaseToast = false,
                xpIncreased = 0
            )
        }
    }
}

data class GenerateViewState(
    val food: FoodViewData = FoodViewData(),
    val isNetworkAvailable: Boolean? = null,
    val foodHasBeenGenerated: Boolean = false,
    val showXpIncreaseToast: Boolean = false,
    val xpIncreased: Int = 0,
)