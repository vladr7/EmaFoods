package com.example.emafoods.feature.profile.presentation.game

import androidx.lifecycle.ViewModel
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.core.extension.capitalizeWords
import com.example.emafoods.feature.profile.domain.usecase.GetListOfXpActionsUseCase
import com.example.emafoods.feature.profile.domain.usecase.GetUserLevelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getListOfXpActionsUseCase: GetListOfXpActionsUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val getUserLevelUseCase: GetUserLevelUseCase
): ViewModel() {

    private val _state = MutableStateFlow<GameViewState>(GameViewState())
    val state: StateFlow<GameViewState> = _state

    init {
        val userDetails = getUserDetailsUseCase.execute()
        val userLevel = getUserLevelUseCase.execute()
        val listOfXpActions = getListOfXpActionsUseCase.execute()
        _state.update {
            it.copy(
                userName = userDetails.displayName.capitalizeWords(),
                userLevel = userLevel.level,
                listOfXpActions = listOfXpActions
            )
        }
    }

    fun onIncreaseXpClick() {
        _state.update {
            it.copy(displayXpAlert = true)
        }
    }

    fun onDismissXpAlertClick() {
        _state.update {
            it.copy(displayXpAlert = false)
        }
    }
}

data class GameViewState(
    val displayXpAlert: Boolean = false,
    val listOfXpActions: List<String> = listOf(),
    val userName: String = "",
    val userLevel: String = "",
)

