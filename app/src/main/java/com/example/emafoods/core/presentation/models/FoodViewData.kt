package com.example.emafoods.core.presentation.models

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.presentation.models.helper.DataModelMapper
import com.example.emafoods.core.presentation.models.helper.ViewData
import com.example.emafoods.feature.addfood.domain.usecase.DeserializeIngredientsUseCase
import com.example.emafoods.feature.addfood.domain.usecase.SerializeIngredientsUseCase
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientMapper
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import javax.inject.Inject

data class FoodViewData(
    val id: String = "",
    val author: String = "",
    val authorUid: String = "",
    val description: String = "",
    val imageRef: String = "",
    val categoryType: CategoryType,
    val ingredients: List<IngredientViewData> = emptyList()
) : ViewData()

class FoodMapper @Inject constructor(
    private val serializeIngredientsUseCase: SerializeIngredientsUseCase,
    private val deserializeIngredientsUseCase: DeserializeIngredientsUseCase,
    private val ingredientMapper: IngredientMapper
) : DataModelMapper<Food, FoodViewData> {

    override fun mapToModel(viewData: FoodViewData): Food {
        val id = viewData.id
        val author = viewData.author
        val description = viewData.description
        val imageRef = viewData.imageRef
        val authorUid = viewData.authorUid
        val ingredients = serializeIngredientsUseCase.execute(viewData.ingredients.map { ingredientMapper.mapToModel(it) })
        val category = viewData.categoryType.string
        return Food(
            id = id,
            author = author,
            authorUid = authorUid,
            description = description,
            imageRef = imageRef,
            category = category,
            ingredients = ingredients
        )
    }

    override fun mapToViewData(model: Food): FoodViewData {
        val id = model.id
        val author = model.author
        val description = model.description
        val imageRef = model.imageRef
        val ingredients = deserializeIngredientsUseCase.execute(model.ingredients)
            .map { ingredientMapper.mapToViewData(it) }
        val categoryType = CategoryType.fromString(model.category)
        return FoodViewData(
            id = id,
            author = author,
            authorUid = model.authorUid,
            description = description,
            imageRef = imageRef,
            categoryType = categoryType,
            ingredients = ingredients
        )
    }
}