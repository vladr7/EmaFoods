package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.feature.addfood.domain.models.Ingredient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DeserializeIngredientsUseCase @Inject constructor(
    private val logHelper: LogHelper
) {

    fun execute(ingredientsStringList: String): List<Ingredient> {
        return try {
            Json.decodeFromString<List<Ingredient>>(ingredientsStringList)
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("Failed to deserialize ingredients list: $ingredientsStringList", e))
            listOf()
        }
    }
}