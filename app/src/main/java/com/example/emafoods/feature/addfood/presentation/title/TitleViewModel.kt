package com.example.emafoods.feature.addfood.presentation.title

import android.net.Uri
import com.example.emafoods.core.presentation.base.BaseViewModel
import com.example.emafoods.core.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TitleViewModel @Inject constructor(

): BaseViewModel() {

    private val _state = MutableStateFlow<TitleViewState>(
        TitleViewState()
    )
    val state: StateFlow<TitleViewState> = _state

}

data class TitleViewState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val hasImage: Boolean = false,
    val imageUri: Uri? = null
) : ViewState(isLoading, errorMessage)