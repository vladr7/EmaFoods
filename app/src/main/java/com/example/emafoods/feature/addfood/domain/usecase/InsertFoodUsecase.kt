package com.example.emafoods.feature.addfood.domain.usecase

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import javax.inject.Inject

class InsertFoodUseCase @Inject constructor(
    private val checkFieldsAreFilledUseCase: CheckFieldsAreFilledUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val addFoodToPendingListUseCase: AddFoodToPendingListUseCase,
) {

    suspend fun execute(
        food: Food,
        fileUri: Uri,
        shouldAddImageFromTemporary: Boolean
    ): State<Food> {
        if (!checkFieldsAreFilledUseCase.execute(food.description)) {
            return State.failed("Te rog adauga o scurta descriere a retetei (minim 10 caractere)")
        }

        if (fileUri == Uri.EMPTY) {
            return State.failed("Te rog adauga o imagine a retetei")
        }

        val user = getUserDetailsUseCase.execute()
        val newFood = Food(
            authorUid = user.uid,
            author = user.displayName,
            description = food.description,
            imageRef = fileUri.toString(),
            category = food.category
        )

        return addFoodToPendingListUseCase.execute(newFood, shouldAddImageFromTemporary)
    }
}

