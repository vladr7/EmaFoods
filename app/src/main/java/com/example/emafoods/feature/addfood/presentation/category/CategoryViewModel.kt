package com.example.emafoods.feature.addfood.presentation.category

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(

) : ViewModel() {


    private val _state = MutableStateFlow<CategoryViewState>(CategoryViewState())
    val state: StateFlow<CategoryViewState> = _state

}

data class CategoryViewState(
    val category: String = ""
)