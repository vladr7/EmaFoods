package com.example.emafoods.core.presentation.models

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.presentation.models.helper.DataModelMapper
import com.example.emafoods.core.presentation.models.helper.ViewData
import javax.inject.Inject

data class FoodViewData(
    val id: String = "",
    val author: String = "",
    val description: String = "",
    val imageRef: String = "",
): ViewData()

class FoodMapper @Inject constructor(): DataModelMapper<Food, FoodViewData> {

    override fun mapToModel(viewData: FoodViewData): Food {
        val id = viewData.id
        val author = viewData.author
        val description = viewData.description
        val imageRef = viewData.imageRef
        return Food(
            id = id,
            author = author,
            description = description,
            imageRef = imageRef
        )
    }

    override fun mapToViewData(model: Food): FoodViewData {
        val id = model.id
        val author = model.author
        val description = model.description
        val imageRef = model.imageRef
        return FoodViewData(
            id = id,
            author = author,
            description = description,
            imageRef = imageRef
        )
    }
}