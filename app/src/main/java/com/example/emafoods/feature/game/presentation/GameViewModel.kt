package com.example.emafoods.feature.game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.core.extension.capitalizeWords
import com.example.emafoods.feature.game.domain.mapper.MapLevelPermissionToViewData
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.example.emafoods.feature.game.domain.usecase.CheckIfAdminCodeIsValidUseCase
import com.example.emafoods.feature.game.domain.usecase.GetLevelPermissionsUseCase
import com.example.emafoods.feature.game.domain.usecase.GetListOfXpActionsUseCase
import com.example.emafoods.feature.game.domain.usecase.GetUserGameDetailsUseCase
import com.example.emafoods.feature.game.domain.usecase.UpgradeBasicUserToAdminUseCase
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
    private val checkIfAdminCodeIsValidUseCase: CheckIfAdminCodeIsValidUseCase,
    private val upgradeBasicUserToAdminUseCase: UpgradeBasicUserToAdminUseCase
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
                    userLevel = userGameDetails.userLevel.uiString,
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

    fun onLadyBugIconClick() {
        _state.update {
            it.copy(showEnterAdminCode = true)
        }
    }

    fun onCodeAlertDimiss() {
        _state.update {
            it.copy(showEnterAdminCode = false)
        }
    }

    fun onCodeEntered(code: String) {
        _state.update {
            it.copy(showEnterAdminCode = false)
        }
        viewModelScope.launch {
            if (checkIfAdminCodeIsValidUseCase.execute(code)) {
                _state.update {
                    it.copy(showUpgradedToAdminAlert = true)
                }
                upgradeBasicUserToAdminUseCase.execute()
            }
        }
    }
}

data class GameViewState(
    val displayXpAlert: Boolean = false,
    val listOfXpActions: List<String> = listOf(),
    val userName: String = "",
    val userLevel: String = "",
    val listOfLevelPermission: List<ViewDataLevelPermission> = listOf(),
    val showEnterAdminCode: Boolean = false,
    val showUpgradedToAdminAlert: Boolean = false
)

data class ViewDataLevelPermission(
    val level: UserLevel,
    val permissions: List<Permission>,
    val hasAccess: Boolean,
    val remainingXp: Long
)
