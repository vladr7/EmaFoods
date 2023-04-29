package com.example.emafoods.feature.generatefood.presentation

import androidx.lifecycle.ViewModel
import com.example.emafoods.core.presentation.models.FoodMapper
import com.example.emafoods.core.presentation.models.FoodViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val foodMapper: FoodMapper,
) : ViewModel() {

    private val _state = MutableStateFlow<GenerateViewState>(GenerateViewState())
    val state: StateFlow<GenerateViewState> = _state
}

data class GenerateViewState(
    val food: FoodViewData = FoodViewData(),
    val isNetworkAvailable: Boolean? = null,
)