package com.example.emafoods.feature.profile.presentation

import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.core.extension.capitalizeWords
import com.example.emafoods.core.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase
): BaseViewModel() {

    private val _state = MutableStateFlow<ProfileViewState>(ProfileViewState())
    val state: StateFlow<ProfileViewState> = _state

    init {
        val userDetails = getUserDetailsUseCase.execute()
        _state.update {
            it.copy(
                userName = userDetails.displayName.capitalizeWords()
            )
        }
    }
}

data class ProfileViewState(
    val userName: String = "",
)