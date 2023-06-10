package com.example.emafoods.feature.addfood.presentation.ingredients.models

import com.example.emafoods.core.presentation.models.helper.ViewData

data class IngredientViewData(
    val id: Long = -1,
    val name: String,
    val measurement: Long
): ViewData()
