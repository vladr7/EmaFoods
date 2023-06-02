package com.example.emafoods.feature.generatefood.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.constants.AnalyticsConstants
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.core.domain.usecase.GetAllFoodsUseCase
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
import kotlinx.coroutines.flow.collectLatest
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
    private val updateFireStreaksUseCase: UpdateFireStreaksUseCase,
    private val getAllFoodsUseCase: GetAllFoodsUseCase,
    private val logHelper: LogHelper
) : ViewModel() {

    private val _state = MutableStateFlow<GenerateViewState>(GenerateViewState())
    val state: StateFlow<GenerateViewState> = _state

    init {
        viewModelScope.launch {
            getAllFoodsUseCase.execute().collectLatest { foods ->
                _state.update {
                    it.copy(
                        listOfFoods = foods
                    )
                }
            }
        }
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
            logHelper.log(AnalyticsConstants.CHECK_FOR_AWAITING_REWARDS)
        }
    }

    private fun refreshFoodsFromRepository() {
        viewModelScope.launch {
            refreshFoodsUseCase.execute()
        }
    }

    fun generateFoodEvent() {
        viewModelScope.launch {
            val food = generateFoodUseCase.execute(
                previousFood = state.value.food,
                foods = state.value.listOfFoods
            )
            _state.update {
                it.copy(
                    food = foodMapper.mapToViewData(food),
                    foodHasBeenGenerated = true,
                )
            }
        }
        viewModelScope.launch {
            logHelper.log(AnalyticsConstants.GENERATE_FOOD)
        }
    }

    fun onXpIncrease(nrOfRewards: Int? = null) {
        viewModelScope.launch {
            val increaseXpActionType = nrOfRewards?.let { IncreaseXpActionType.RECIPE_ACCEPTED }
                ?: IncreaseXpActionType.GENERATE_RECIPE
            when (val result = increaseXpUseCase.execute(increaseXpActionType, nrOfRewards ?: 0)) {
                is IncreaseXpResult.ExceededUnspentThreshold -> {
                    _state.update {
                        it.copy(
                            showXpIncreaseToast = true,
                            xpIncreased = result.data
                        )
                    }
                    logHelper.log(AnalyticsConstants.EXCEEDED_UNSPENT_THRESHOLD)
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
                    logHelper.log(AnalyticsConstants.LEVELED_UP)
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
        viewModelScope.launch {
            logHelper.log(AnalyticsConstants.XP_INCREASE_TOAST_SHOWN)
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
    val xpIncreased: Long = 0,
    val leveledUpEvent: Boolean = false,
    val newLevel: UserLevel? = null,
    val appOpenedToday: Boolean = true,
    val showRewardsAlert: Boolean = false,
    val nrOfRewards: Int = 0,
    val listOfFoods: List<Food> = emptyList()
)