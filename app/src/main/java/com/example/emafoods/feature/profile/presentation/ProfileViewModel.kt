package com.example.emafoods.feature.profile.presentation

import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.core.extension.capitalizeWords
import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.feature.game.domain.usecase.GetConsecutiveDaysAppOpenedUseCase
import com.example.emafoods.feature.game.domain.usecase.IncreaseXpUseCase
import com.example.emafoods.feature.game.presentation.enums.IncreaseXpActionType
import com.example.emafoods.feature.game.presentation.model.IncreaseXpResult
import com.example.emafoods.feature.profile.domain.usecase.SignOutUseCase
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
    private val consecutiveDaysAppOpenedUseCase: GetConsecutiveDaysAppOpenedUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<ProfileViewState>(ProfileViewState())
    val state: StateFlow<ProfileViewState> = _state

    init {
        getUserName()
        getStreaks()
    }

    private fun getUserName() {
        val userDetails = getUserDetailsUseCase.execute()
        _state.update {
            it.copy(
                userName = userDetails.displayName.capitalizeWords()
            )
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
            when (val result = increaseXpUseCase.execute(IncreaseXpActionType.ADD_REVIEW)) {
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

                is IncreaseXpResult.LeveledUp -> TODO()
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
    }
}

data class ProfileViewState(
    val signOutConfirmed: Boolean = false,
    val showSignOutAlert: Boolean = false,
    val userName: String = "",
    val showXpIncreaseToast: Boolean = false,
    val xpIncreased: Int = 0,
    val streaks: Int = 1
)