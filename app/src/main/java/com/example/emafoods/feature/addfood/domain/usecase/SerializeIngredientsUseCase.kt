package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.feature.addfood.domain.models.Ingredient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SerializeIngredientsUseCase @Inject constructor(
    private val logHelper: LogHelper
) {

    fun execute(ingredientsList: List<Ingredient>): String {
        return try {
            Json.encodeToString<List<Ingredient>>(ingredientsList)
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("Failed to serialize ingredients list: $ingredientsList", e))
            ""
        }
    }
}