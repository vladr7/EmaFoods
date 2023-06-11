package com.example.emafoods.feature.addfood.domain.models

import com.example.emafoods.core.data.models.helper.Model
import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val id: Long = -1,
    val name: String,
    val measurement: Long
): Model()
