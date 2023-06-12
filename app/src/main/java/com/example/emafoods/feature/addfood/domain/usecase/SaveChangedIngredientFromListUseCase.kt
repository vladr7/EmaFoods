package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.feature.addfood.domain.models.IngredientResult
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import javax.inject.Inject

class SaveChangedIngredientFromListUseCase @Inject constructor() {

    fun execute(ingredient: IngredientViewData, ingredientsList: List<IngredientViewData>): IngredientResult<List<IngredientViewData>> {
        val newList = ingredientsList.map { currentIngredient ->
            if (currentIngredient.id == ingredient.id) {
                ingredient
            } else {
                currentIngredient
            }
        }
        return IngredientResult.Success(newList)
    }
}