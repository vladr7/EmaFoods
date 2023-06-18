package com.example.emafoods.feature.addfood.domain.usecase

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientMapper
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import javax.inject.Inject

class InsertFoodUseCase @Inject constructor(
    private val checkFieldsAreFilledUseCase: CheckFieldsAreFilledUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val addFoodToPendingListUseCase: AddFoodToPendingListUseCase,
    private val serializeIngredientsUseCase: SerializeIngredientsUseCase,
    private val ingredientsMapper: IngredientMapper
) {

    suspend fun execute(
        food: Food,
        fileUri: Uri,
        shouldAddImageFromTemporary: Boolean,
        ingredients: List<IngredientViewData>
    ): State<Food> {
        when(val result = checkFieldsAreFilledUseCase.execute(
            foodDescription = food.description,
            title = food.title,
            fileUri = fileUri,
            ingredients = ingredients
        )) {
            is State.Failed -> {
                return State.failed(result.message)
            }
            is State.Success -> {
                val user = getUserDetailsUseCase.execute()
                val serializedIngredients = serializeIngredientsUseCase.execute(
                    ingredients.map { ingredientsMapper.mapToModel(it) }
                )
                val newFood = Food(
                    authorUid = user.uid,
                    author = user.displayName,
                    description = food.description,
                    imageRef = fileUri.toString(),
                    category = food.category,
                    ingredients = serializedIngredients
                )
                return addFoodToPendingListUseCase.execute(newFood, shouldAddImageFromTemporary)
            }
        }
    }
}

