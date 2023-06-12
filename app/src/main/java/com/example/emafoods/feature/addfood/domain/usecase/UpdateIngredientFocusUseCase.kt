package com.example.emafoods.feature.addfood.domain.usecase

import com.example.emafoods.feature.addfood.domain.models.IngredientResult
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import javax.inject.Inject

class UpdateIngredientFocusUseCase @Inject constructor() {

    fun execute(
        ingredientList: List<IngredientViewData>,
        ingredient: IngredientViewData,
        isFocused: Boolean
    ): IngredientResult<List<IngredientViewData>> {
        val newList = ingredientList.map { ingredientViewData ->
            if (ingredientViewData.id == ingredient.id) {
                ingredientViewData.copy(isFocused = isFocused)
            } else {
                ingredientViewData
            }
        }
        return IngredientResult.Success(newList)
    }
}