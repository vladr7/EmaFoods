package com.example.emafoods.feature.generatefood.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.R
import com.example.emafoods.core.domain.constants.AnalyticsConstants
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.core.domain.usecase.GetAllFoodsUseCase
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.core.domain.usecase.RefreshFoodsUseCase
import com.example.emafoods.core.extension.capitalizeWords
import com.example.emafoods.core.presentation.models.FoodMapper
import com.example.emafoods.core.presentation.models.FoodViewData
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.domain.usecase.GetConsecutiveDaysAppOpenedUseCase
import com.example.emafoods.feature.game.domain.usecase.GetUserRewardsUseCase
import com.example.emafoods.feature.game.domain.usecase.IncreaseXpUseCase
import com.example.emafoods.feature.game.domain.usecase.ResetUserRewardsUseCase
import com.example.emafoods.feature.game.domain.usecase.UpdateFireStreaksUseCase
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import com.example.emafoods.feature.game.presentation.model.IncreaseXpResult
import com.example.emafoods.feature.generatefood.domain.usecase.GenerateFoodUseCase
import com.example.emafoods.feature.generatefood.domain.usecase.GetFoodsByCategoryUseCase
import com.example.emafoods.feature.generatefood.domain.usecase.PreviousFoodUseCase
import com.example.emafoods.feature.profile.domain.models.ProfileImage
import com.example.emafoods.feature.profile.domain.usecase.GetCurrentProfileImageUseCase
import com.example.emafoods.feature.profile.domain.usecase.GetNextProfileImageUseCase
import com.example.emafoods.feature.profile.domain.usecase.StoreProfileImageChoiceUseCase
import com.example.emafoods.feature.profile.domain.usecase.UpdateUsernameUseCase
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
    private val getFoodsByCategoryUseCase: GetFoodsByCategoryUseCase,
    private val previousFoodUseCase: PreviousFoodUseCase,
    private val consecutiveDaysAppOpenedUseCase: GetConsecutiveDaysAppOpenedUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val getNextProfileImageUseCase: GetNextProfileImageUseCase,
    private val storeProfileImageChoiceUseCase: StoreProfileImageChoiceUseCase,
    private val getCurrentProfileImageUseCase: GetCurrentProfileImageUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<GenerateViewState>(GenerateViewState())
    val state: StateFlow<GenerateViewState> = _state

    init {
        getAllFoods()
        updateFireStreaks()
        checkForAwaitingRewards()
        refreshFoodsFromRepository()
        getUserName()
        getStreaks()
        getCurrentProfileImage()
    }

    private fun getCurrentProfileImage() {
        viewModelScope.launch {
            val currentProfileImage = getCurrentProfileImageUseCase.execute()
            _state.update {
                it.copy(
                    profileImage = currentProfileImage
                )
            }
        }
    }

    private fun getUserName() {
        viewModelScope.launch {
            val userDetails = getUserDetailsUseCase.execute()
            _state.update {
                it.copy(
                    userName = userDetails.displayName.capitalizeWords()
                )
            }
        }
    }

    private fun getStreaks() {
        viewModelScope.launch {
            val streaks = consecutiveDaysAppOpenedUseCase.execute()
            _state.update {
                it.copy(
                    nrOfFireStreaks = streaks
                )
            }
        }
    }

    private fun updateFireStreaks() {
        viewModelScope.launch {
            updateFireStreaksUseCase.execute()
            getStreaks()
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

    fun onDismissCategoryDropDown() {
        _state.update {
            it.copy(
                categoryDropdownExpanded = false
            )
        }
    }

    fun onClickCategoryDropDown() {
        _state.update {
            it.copy(
                categoryDropdownExpanded = !it.categoryDropdownExpanded
            )
        }
    }

    fun generateFoodEvent() {
        when (state.value.currentCategory) {
            CategoryType.BREAKFAST -> {
                if (state.value.breakfastList.isEmpty()) {
                    showEmptyListToast()
                    return
                }
                val nextFood = generateFoodUseCase.execute(
                    foodList = state.value.breakfastList,
                    index = state.value.indexBreakfast
                )
                val newCounter = state.value.counterBreakfast + 1
                val newIndex = state.value.indexBreakfast + 1
                _state.update {
                    it.copy(
                        indexBreakfast = if (newIndex > state.value.breakfastList.size - 1) 0 else newIndex,
                        counterBreakfast = newCounter,
                        currentFood = nextFood,
                        previousButtonVisible = newCounter > 1
                    )
                }
            }

            CategoryType.MAIN_DISH -> {
                if (state.value.mainDishList.isEmpty()) {
                    showEmptyListToast()
                    return
                }
                val nextFood = generateFoodUseCase.execute(
                    foodList = state.value.mainDishList,
                    index = state.value.indexMainDish
                )
                val newCounter = state.value.counterMainDish + 1
                val newIndex = state.value.indexMainDish + 1
                _state.update {
                    it.copy(
                        indexMainDish = if (newIndex > state.value.mainDishList.size - 1) 0 else newIndex,
                        counterMainDish = newCounter,
                        currentFood = nextFood,
                        previousButtonVisible = newCounter > 1
                    )
                }
            }

            CategoryType.SOUP -> {
                if (state.value.soupList.isEmpty()) {
                    showEmptyListToast()
                    return
                }
                val nextFood = generateFoodUseCase.execute(
                    foodList = state.value.soupList,
                    index = state.value.indexSoup
                )
                val newCounter = state.value.counterSoup + 1
                val newIndex = state.value.indexSoup + 1
                _state.update {
                    it.copy(
                        indexSoup = if (newIndex > state.value.soupList.size - 1) 0 else newIndex,
                        counterSoup = newCounter,
                        currentFood = nextFood,
                        previousButtonVisible = newCounter > 1
                    )
                }
            }

            CategoryType.DESSERT -> {
                if (state.value.dessertList.isEmpty()) {
                    showEmptyListToast()
                    return
                }
                val nextFood = generateFoodUseCase.execute(
                    foodList = state.value.dessertList,
                    index = state.value.indexDessert
                )
                val newCounter = state.value.counterDessert + 1
                val newIndex = state.value.indexDessert + 1
                _state.update {
                    it.copy(
                        indexDessert = if (newIndex > state.value.dessertList.size - 1) 0 else newIndex,
                        counterDessert = newCounter,
                        currentFood = nextFood,
                        previousButtonVisible = newCounter > 1
                    )
                }
            }
        }
        viewModelScope.launch {
            logHelper.log(AnalyticsConstants.GENERATE_FOOD)
        }
    }

    fun onPreviousButtonClick() {
        when (state.value.currentCategory) {
            CategoryType.BREAKFAST -> {
                if (state.value.breakfastList.isEmpty()) {
                    showEmptyListToast()
                    return
                }
                if (state.value.counterBreakfast < 1) {
                    return
                }
                val previousFood = previousFoodUseCase.execute(
                    foodList = state.value.breakfastList,
                    index = state.value.indexBreakfast
                )
                val newCounter = state.value.counterBreakfast - 1
                val newIndex = state.value.indexBreakfast - 1
                _state.update {
                    it.copy(
                        currentFood = previousFood,
                        indexBreakfast = if (newIndex < 0) it.breakfastList.size - 1 else newIndex,
                        counterBreakfast = newCounter,
                        previousButtonVisible = newCounter > 1
                    )
                }
            }

            CategoryType.MAIN_DISH -> {
                if (state.value.mainDishList.isEmpty()) {
                    showEmptyListToast()
                    return
                }
                if (state.value.counterMainDish < 1) {
                    return
                }
                val previousFood = previousFoodUseCase.execute(
                    foodList = state.value.mainDishList,
                    index = state.value.indexMainDish
                )
                val newCounter = state.value.counterMainDish - 1
                val newIndex = state.value.indexMainDish - 1
                _state.update {
                    it.copy(
                        currentFood = previousFood,
                        indexMainDish = if (newIndex < 0) it.mainDishList.size - 1 else newIndex,
                        counterMainDish = newCounter,
                        previousButtonVisible = newCounter > 1
                    )
                }
            }

            CategoryType.SOUP -> {
                if (state.value.soupList.isEmpty()) {
                    showEmptyListToast()
                    return
                }
                if (state.value.counterSoup < 1) {
                    return
                }
                val previousFood = previousFoodUseCase.execute(
                    foodList = state.value.soupList,
                    index = state.value.indexSoup
                )
                val newCounter = state.value.counterSoup - 1
                val newIndex = state.value.indexSoup - 1
                _state.update {
                    it.copy(
                        currentFood = previousFood,
                        indexSoup = if (newIndex < 0) it.soupList.size - 1 else newIndex,
                        counterSoup = newCounter,
                        previousButtonVisible = newCounter > 1
                    )
                }
            }

            CategoryType.DESSERT -> {
                val currentCounter = state.value.counterDessert
                if (state.value.dessertList.isEmpty()) {
                    showEmptyListToast()
                    return
                }
                if (currentCounter < 1) {
                    return
                }
                val previousFood = previousFoodUseCase.execute(
                    foodList = state.value.dessertList,
                    index = state.value.indexDessert
                )
                val newCounter = state.value.counterDessert - 1
                val newIndex = state.value.indexDessert - 1
                _state.update {
                    it.copy(
                        currentFood = previousFood,
                        indexDessert = if (newIndex < 0) it.dessertList.size - 1 else newIndex,
                        counterDessert = newCounter,
                        previousButtonVisible = newCounter > 1
                    )
                }
            }
        }

    }

    private fun showEmptyListToast() {
        _state.update {
            it.copy(
                showEmptyListToast = true
            )
        }
    }

    fun onProfileImageClick(profileImage: ProfileImage) {
        viewModelScope.launch {
            val nextProfileImage = getNextProfileImageUseCase.execute(profileImage)
            _state.update {
                it.copy(
                    profileImage = nextProfileImage
                )
            }
            storeProfileImageChoiceUseCase.execute(nextProfileImage)
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    categorySelected = false
                )
            }
        }
    }

    fun onShowNewUsernameDialog() {
        _state.update {
            it.copy(
                showNewUsernameDialog = true
            )
        }
    }

    fun onDismissNewUsernameDialog() {
        _state.update {
            it.copy(
                showNewUsernameDialog = false
            )
        }
    }

    fun onConfirmNewUsernameDialog(newUserName: String) {
        viewModelScope.launch {
            updateUsernameUseCase.execute(newUserName)
            _state.update {
                it.copy(
                    userName = newUserName.capitalizeWords(),
                    showNewUsernameDialog = false
                )
            }
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
    val counterMainDish: Int = 0,
    val indexDessert: Int = 0,
    val counterDessert: Int = 0,
    val indexBreakfast: Int = 0,
    val counterBreakfast: Int = 0,
    val indexSoup: Int = 0,
    val counterSoup: Int = 0,
    val showCategories: Boolean = false,
    val showEmptyListToast: Boolean = false,
    val categoryDropdownExpanded: Boolean = false,
    val previousButtonVisible: Boolean = false,
    val userName: String = "",
    val nrOfFireStreaks: Int = 0,
    val profileImage: ProfileImage = ProfileImage(
        id = 0,
        image = R.drawable.profilepic1,
    ),
    val showNewUsernameDialog: Boolean = false
)
