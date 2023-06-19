package com.example.emafoods.feature.addfood.domain.usecase

import android.content.Context
import android.net.Uri
import com.example.emafoods.R
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientViewData
import javax.inject.Inject

class CheckFieldsAreFilledUseCase @Inject constructor(
    private val context: Context
) {

    fun execute(
        foodDescription: String,
        title: String,
        fileUri: Uri,
        ingredients: List<IngredientViewData>
    ): State<String> {
        if(fileUri.toString().isEmpty())
            return State.failed(context.getString(R.string.please_add_image))
        if(foodDescription.length < 10)
            return State.failed(context.getString(R.string.please_add_description))
        if(title.length < 5)
            return State.failed(context.getString(R.string.please_add_title))
        if(ingredients.isEmpty())
            return State.failed(context.getString(R.string.please_add_ingredient))
        return State.success(context.getString(R.string.all_fields_are_filled))
    }
}