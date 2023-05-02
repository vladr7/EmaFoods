package com.example.emafoods.core.presentation.base

import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {

    open fun showError(errorMessage: String) {}

    open fun hideError() {}

    open fun showLoading() {}

    open fun hideLoading() {}

}