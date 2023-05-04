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

    fun onTitleChange(title: String) {
        _state.value = _state.value.copy(title = title)
    }
}

data class TitleViewState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val imageUri: Uri? = null,
    val title: String = ""
) : ViewState(isLoading, errorMessage)