package com.example.emafoods.core.presentation.models

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.presentation.models.helper.DataModelMapper
import com.example.emafoods.core.presentation.models.helper.ViewData
import javax.inject.Inject

data class FoodViewData(
    val author: String = "",
    val description: String = "",
    val imageRef: String = "",
): ViewData()

class FoodMapper @Inject constructor(): DataModelMapper<Food, FoodViewData> {

    override fun mapToModel(viewData: FoodViewData): Food {
        TODO("Not yet implemented")
    }

    override fun mapToViewData(model: Food): FoodViewData {
        val description = model.description
        val imageRef = model.imageRef
        return FoodViewData(description, imageRef)
    }

}