package com.example.emafoods.feature.profile.presentation

import androidx.lifecycle.viewModelScope
import com.example.emafoods.R
import com.example.emafoods.core.domain.constants.AnalyticsConstants
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.core.extension.capitalizeWords
import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.domain.usecase.CheckXNrOfDaysPassedSinceLastReviewUseCase
import com.example.emafoods.feature.game.domain.usecase.GetConsecutiveDaysAppOpenedUseCase
import com.example.emafoods.feature.game.domain.usecase.IncreaseXpUseCase
import com.example.emafoods.feature.game.domain.usecase.UpdateLastTimeUserReviewedUseCase
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import com.example.emafoods.feature.game.presentation.model.IncreaseXpResult
import com.example.emafoods.feature.profile.domain.models.ProfileImage
import com.example.emafoods.feature.profile.domain.usecase.GetCurrentProfileImageUseCase
import com.example.emafoods.feature.profile.domain.usecase.GetNextProfileImageUseCase
import com.example.emafoods.feature.profile.domain.usecase.SignOutUseCase
import com.example.emafoods.feature.profile.domain.usecase.StoreProfileImageChoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val increaseXpUseCase: IncreaseXpUseCase,
    private val consecutiveDaysAppOpenedUseCase: GetConsecutiveDaysAppOpenedUseCase,
    private val checkXNrOfDaysPassedSinceLastReviewUseCase: CheckXNrOfDaysPassedSinceLastReviewUseCase,
    private val updateLastTimeUserReviewedUseCase: UpdateLastTimeUserReviewedUseCase,
    private val logHelper: LogHelper,
    private val getNextProfileImageUseCase: GetNextProfileImageUseCase,
    private val storeProfileImageChoiceUseCase: StoreProfileImageChoiceUseCase,
    private val getCurrentProfileImageUseCase: GetCurrentProfileImageUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<ProfileViewState>(ProfileViewState())
    val state: StateFlow<ProfileViewState> = _state

    init {
        getUserName()
        getStreaks()
        getCurrentProfileImage()
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
                    streaks = streaks
                )
            }
        }
    }

    fun onSignOutClick() {
        _state.update {
            it.copy(
                showSignOutAlert = true
            )
        }
        viewModelScope.launch {
            logHelper.log(AnalyticsConstants.CLICKED_ON_SIGN_OUT)
        }
    }

    fun onDismissSignOutAlert() {
        _state.update {
            it.copy(
                showSignOutAlert = false
            )
        }
    }

    fun onConfirmSignOut() {
        signOutUseCase.execute()
        _state.update {
            it.copy(
                signOutConfirmed = true
            )
        }
    }

    override fun onXpIncrease() {
        viewModelScope.launch {
            if(checkXNrOfDaysPassedSinceLastReviewUseCase.execute(7)) {
                updateLastTimeUserReviewedUseCase.execute()
                when (val result = increaseXpUseCase.execute(IncreaseXpActionType.ADD_REVIEW)) {
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
    }

    override fun onXpIncreaseToastShown() {
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

    fun onLevelUpClick() {
        viewModelScope.launch {
            logHelper.log(AnalyticsConstants.CLICKED_ON_MY_LEVEL_BUTTON)
        }
    }

    fun onReviewClick() {
        viewModelScope.launch {
            logHelper.log(AnalyticsConstants.CLICKED_ON_REVIEW)
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
}

data class ProfileViewState(
    val signOutConfirmed: Boolean = false,
    val showSignOutAlert: Boolean = false,
    val userName: String = "",
    val showXpIncreaseToast: Boolean = false,
    val xpIncreased: Long = 0,
    val streaks: Int = 1,
    val leveledUpEvent: Boolean = false,
    val newLevel: UserLevel? = null,
    val profileImage: ProfileImage = ProfileImage(
        id = 0,
        image = R.drawable.profilepic1,
    )
)
