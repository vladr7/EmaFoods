package com.example.emafoods.feature.addfood.presentation.ingredients.models

import com.example.emafoods.core.presentation.models.helper.DataModelMapper
import com.example.emafoods.core.presentation.models.helper.ViewData
import com.example.emafoods.feature.addfood.domain.models.Ingredient
import javax.inject.Inject

data class IngredientViewData(
    val id: Long = -1,
    val name: String,
    val measurement: Long
) : ViewData()

class IngredientMapper @Inject constructor() : DataModelMapper<Ingredient, IngredientViewData> {

    override fun mapToModel(viewData: IngredientViewData): Ingredient {
        val id = viewData.id
        val name = viewData.name
        val measurement = viewData.measurement
        return Ingredient(
            id = id,
            name = name,
            measurement = measurement
        )
    }

    override fun mapToViewData(model: Ingredient): IngredientViewData {
        val id = model.id
        val name = model.name
        val measurement = model.measurement
        return IngredientViewData(
            id = id,
            name = name,
            measurement = measurement
        )
    }
}