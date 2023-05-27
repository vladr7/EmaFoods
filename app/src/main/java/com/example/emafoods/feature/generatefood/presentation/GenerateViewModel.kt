package com.example.emafoods.feature.generatefood.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.usecase.RefreshFoodsUseCase
import com.example.emafoods.core.presentation.models.FoodMapper
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.domain.usecase.GetUserRewardsUseCase
import com.example.emafoods.feature.game.domain.usecase.IncreaseXpUseCase
import com.example.emafoods.feature.game.domain.usecase.ResetUserRewardsUseCase
import com.example.emafoods.feature.game.domain.usecase.UpdateFireStreaksUseCase
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import com.example.emafoods.feature.game.presentation.model.IncreaseXpResult
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
    private val increaseXpUseCase: IncreaseXpUseCase,
    private val getUserRewardsUseCase: GetUserRewardsUseCase,
    private val resetUserRewardsUseCase: ResetUserRewardsUseCase,
    private val updateFireStreaksUseCase: UpdateFireStreaksUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<GenerateViewState>(GenerateViewState())
    val state: StateFlow<GenerateViewState> = _state

    init {
        viewModelScope.launch {
            updateFireStreaksUseCase.execute()
        }
        checkForAwaitingRewards()
        refreshFoodsFromRepository()
    }

    private fun checkForAwaitingRewards() {
        viewModelScope.launch {
            val rewards = getUserRewardsUseCase.execute().toInt()
            if (rewards != 0) {
                onXpIncrease(nrOfRewards = rewards)
                _state.update {
                    it.copy(
                        showRewardsAlert = true,
                        nrOfRewards = rewards
                    )
                }
                resetUserRewardsUseCase.execute()
            }
        }
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

    fun onXpIncrease(nrOfRewards: Int? = null) {
        viewModelScope.launch {
            val increaseXpActionType = nrOfRewards?.let { IncreaseXpActionType.RECIPE_ACCEPTED } ?: IncreaseXpActionType.GENERATE_RECIPE
            when (val result = increaseXpUseCase.execute(increaseXpActionType, nrOfRewards ?: 0)) {
                is IncreaseXpResult.ExceededUnspentThreshold -> {
                    _state.update {
                        it.copy(
                            showXpIncreaseToast = true,
                            xpIncreased = result.data
                        )
                    }
                }

                is IncreaseXpResult.NotExceededUnspentThreshold -> {
                    _state.update {
                        it.copy(
                            showXpIncreaseToast = false,
                            xpIncreased = 0
                        )
                    }
                }

                is IncreaseXpResult.LeveledUp -> {
                    _state.update {
                        it.copy(
                            leveledUpEvent = true,
                            newLevel = result.levelAcquired
                        )
                    }
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

    fun onDismissLevelUp() {
        _state.update {
            it.copy(
                leveledUpEvent = false,
                newLevel = null
            )
        }
    }

    fun onDismissRewardsAlert() {
        _state.update {
            it.copy(
                showRewardsAlert = false,
                nrOfRewards = 0
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
    val leveledUpEvent: Boolean = false,
    val newLevel: UserLevel? = null,
    val appOpenedToday: Boolean = true,
    val showRewardsAlert: Boolean = false,
    val nrOfRewards: Int = 0,
)