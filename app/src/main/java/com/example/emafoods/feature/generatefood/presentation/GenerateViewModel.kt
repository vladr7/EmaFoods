package com.example.emafoods.feature.generatefood.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.constants.AnalyticsConstants
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.core.domain.usecase.GetAllFoodsUseCase
import com.example.emafoods.core.domain.usecase.RefreshFoodsUseCase
import com.example.emafoods.core.presentation.models.FoodMapper
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.domain.usecase.GetUserRewardsUseCase
import com.example.emafoods.feature.game.domain.usecase.IncreaseXpUseCase
import com.example.emafoods.feature.game.domain.usecase.ResetUserRewardsUseCase
import com.example.emafoods.feature.game.domain.usecase.UpdateFireStreaksUseCase
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import com.example.emafoods.feature.game.presentation.model.IncreaseXpResult
import com.example.emafoods.feature.generatefood.domain.models.GenerateResult
import com.example.emafoods.feature.generatefood.domain.usecase.GenerateFoodUseCase
import com.example.emafoods.feature.generatefood.domain.usecase.GetFoodsByCategoryUseCase
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
    private val logHelper: LogHelper,
    private val getFoodsByCategoryUseCase: GetFoodsByCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<GenerateViewState>(GenerateViewState())
    val state: StateFlow<GenerateViewState> = _state

    init {
        getAllFoods()
        updateFireStreaks()
        checkForAwaitingRewards()
        refreshFoodsFromRepository()
    }

    private fun updateFireStreaks() {
        viewModelScope.launch {
            updateFireStreaksUseCase.execute()
        }
    }

    private fun getAllFoods() {
        viewModelScope.launch {
            getAllFoodsUseCase.execute().collectLatest { foods ->
                val mappedFoods = foods.map { food ->
                    foodMapper.mapToViewData(food)
                }
                val mainDishList = getFoodsByCategoryUseCase.execute(
                    foods = mappedFoods,
                    categoryType = CategoryType.MAIN_DISH
                )
                val dessertList = getFoodsByCategoryUseCase.execute(
                    foods = mappedFoods,
                    categoryType = CategoryType.DESSERT
                )
                val breakfastList = getFoodsByCategoryUseCase.execute(
                    foods = mappedFoods,
                    categoryType = CategoryType.BREAKFAST
                )
                val soupList = getFoodsByCategoryUseCase.execute(
                    foods = mappedFoods,
                    categoryType = CategoryType.SOUP
                )
                _state.update {
                    it.copy(
                        mainDishList = mainDishList,
                        dessertList = dessertList,
                        breakfastList = breakfastList,
                        soupList = soupList
                    )
                }
            }
        }
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
            when(val result = generateFoodUseCase.execute(
                categoryType = state.value.currentCategory,
                mainDishList = state.value.mainDishList,
                dessertList = state.value.dessertList,
                breakfastList = state.value.breakfastList,
                soupList = state.value.soupList,
                indexMainDish = state.value.indexMainDish,
                indexDessert = state.value.indexDessert,
                indexBreakfast = state.value.indexBreakfast,
                indexSoup = state.value.indexSoup
            ) ) {
                is GenerateResult.Success -> {
                    _state.update {
                        it.copy(
                            currentFood = result.food,
                        )
                    }
                    when(state.value.currentCategory) {
                        CategoryType.MAIN_DISH -> {
                            _state.update {
                                it.copy(
                                    indexMainDish = it.indexMainDish + 1
                                )
                            }
                        }
                        CategoryType.DESSERT -> {
                            _state.update {
                                it.copy(
                                    indexDessert = it.indexDessert + 1
                                )
                            }
                        }
                        CategoryType.BREAKFAST -> {
                            _state.update {
                                it.copy(
                                    indexBreakfast = it.indexBreakfast + 1
                                )
                            }
                        }
                        CategoryType.SOUP -> {
                            _state.update {
                                it.copy(
                                    indexSoup = it.indexSoup + 1
                                )
                            }
                        }
                    }
                }
                is GenerateResult.SuccessAndIndexMustBeReset -> {
                    _state.update {
                        it.copy(
                            currentFood = result.food,
                        )
                    }
                    when(state.value.currentCategory) {
                        CategoryType.BREAKFAST -> {
                            _state.update {
                                it.copy(
                                    indexBreakfast = 0
                                )
                            }
                        }
                        CategoryType.MAIN_DISH -> {
                            _state.update {
                                it.copy(
                                    indexMainDish = 0
                                )
                            }
                        }
                        CategoryType.SOUP -> {
                            _state.update {
                                it.copy(
                                    indexSoup = 0
                                )
                            }
                        }
                        CategoryType.DESSERT -> {
                            _state.update {
                                it.copy(
                                    indexDessert = 0
                                )
                            }
                        }
                    }
                }
                is GenerateResult.ErrorEmptyList -> {
                    _state.update {
                        it.copy(
                            showEmptyListToast = true
                        )
                    }
                }
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

    fun onCategoryClick() {
        _state.update {
            it.copy(
                showCategories = !it.showCategories
            )
        }
    }

    fun onCategorySelected(categoryType: CategoryType) {
        _state.update {
            it.copy(
                showCategories = false,
                categorySelected = true,
                currentCategory = categoryType
            )
        }
        generateFoodEvent()
    }

    fun onShowedEmptyListToast() {
        _state.update {
            it.copy(
                showEmptyListToast = false
            )
        }
    }
}

data class GenerateViewState(
    val currentFood: FoodViewData = FoodViewData(categoryType = CategoryType.MAIN_DISH),
    val currentCategory: CategoryType = CategoryType.MAIN_DISH,
    val isNetworkAvailable: Boolean? = null,
    val categorySelected: Boolean = false,
    val showXpIncreaseToast: Boolean = false,
    val xpIncreased: Long = 0,
    val leveledUpEvent: Boolean = false,
    val newLevel: UserLevel? = null,
    val appOpenedToday: Boolean = true,
    val showRewardsAlert: Boolean = false,
    val nrOfRewards: Int = 0,
    val mainDishList: List<FoodViewData> = emptyList(),
    val dessertList: List<FoodViewData> = emptyList(),
    val breakfastList: List<FoodViewData> = emptyList(),
    val soupList: List<FoodViewData> = emptyList(),
    val indexMainDish: Int = 0,
    val indexDessert: Int = 0,
    val indexBreakfast: Int = 0,
    val indexSoup: Int = 0,
    val showCategories: Boolean = false,
    val showEmptyListToast: Boolean = false
)
