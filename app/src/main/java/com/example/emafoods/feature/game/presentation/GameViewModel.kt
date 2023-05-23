package com.example.emafoods.feature.game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.core.extension.capitalizeWords
import com.example.emafoods.feature.game.domain.mapper.MapLevelPermissionToViewData
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.domain.usecase.GetLevelPermissionsUseCase
import com.example.emafoods.feature.game.domain.usecase.GetListOfXpActionsUseCase
import com.example.emafoods.feature.game.domain.usecase.GetUserGameDetailsUseCase
import com.example.emafoods.feature.game.domain.usecase.StoreUserLevelUseCase
import com.example.emafoods.feature.game.presentation.model.Permission
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getListOfXpActionsUseCase: GetListOfXpActionsUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val getLevelPermissionsUseCase: GetLevelPermissionsUseCase,
    private val mapLevelPermissionToViewData: MapLevelPermissionToViewData,
    private val getUserGameDetailsUseCase: GetUserGameDetailsUseCase,
    private val storeUserLevelUseCase: StoreUserLevelUseCase
): ViewModel() {

    private val _state = MutableStateFlow<GameViewState>(GameViewState())
    val state: StateFlow<GameViewState> = _state

    init {
        viewModelScope.launch {
            val levelPermissions = mapLevelPermissionToViewData.execute(
                getLevelPermissionsUseCase.execute()
            )
            val userGameDetails = getUserGameDetailsUseCase.execute()
            val userDetails = getUserDetailsUseCase.execute()
            val listOfXpActions = getListOfXpActionsUseCase.execute()
            _state.update {
                it.copy(
                    userName = userDetails.displayName.capitalizeWords(),
                    userLevel = userGameDetails.userLevel.string,
                    listOfXpActions = listOfXpActions,
                    listOfLevelPermission = levelPermissions
                )
            }
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
    val listOfLevelPermission: List<ViewDataLevelPermission> = listOf(),
)

data class ViewDataLevelPermission(
    val levelName: String,
    val permissions: List<Permission>,
    val hasAccess: Boolean,
    val remainingXp: Int
)
