package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.feature.addfood.domain.models.IngredientResult
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import javax.inject.Inject

class RemoveIngredientFromListUseCase @Inject constructor() {

    fun execute(ingredient: IngredientViewData, ingredientsList: List<IngredientViewData>): IngredientResult<List<IngredientViewData>> {
        return IngredientResult.Success(ingredientsList - ingredient)
    }
}