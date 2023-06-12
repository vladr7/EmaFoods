package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.feature.addfood.domain.models.IngredientResult
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import javax.inject.Inject

class AddIngredientToListUseCase @Inject constructor() {

    fun execute(ingredient: IngredientViewData, ingredientsList: List<IngredientViewData>): IngredientResult<List<IngredientViewData>> {
        return if (ingredientsList.contains(ingredient)) {
            IngredientResult.ErrorAlreadyAdded(Unit)
        } else {
            IngredientResult.Success(ingredientsList + ingredient.copy(id = getNextIngredientId(ingredientsList)))
        }
    }
}

private fun getNextIngredientId(ingredientsList: List<IngredientViewData>): Long {
    return ingredientsList.maxOfOrNull { it.id }?.plus(1) ?: 1
}