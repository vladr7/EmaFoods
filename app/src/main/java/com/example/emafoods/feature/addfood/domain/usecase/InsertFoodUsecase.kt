package com.example.emafoods.feature.addfood.domain.usecase

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.models.UserType
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.core.utils.State
import javax.inject.Inject

class InsertFoodUseCase @Inject constructor(
    private val checkFieldsAreFilledUseCase: CheckFieldsAreFilledUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val addFoodToMainListUseCase: AddFoodToMainListUseCase,
    private val addFoodToPendingListUseCase: AddFoodToPendingListUseCase,
) {

    suspend fun execute(food: Food, imageUri: Uri?): State<Food> {
        if(!checkFieldsAreFilledUseCase.execute(food.description)) {
            return State.failed("Te rog adauga o scurta descriere a retetei (minim 10 caractere)")
        }

        if(imageUri == null) {
            return State.failed("Te rog adauga o imagine a retetei")
        }

       val user = getUserDetailsUseCase.execute()
        val newFood = Food(
            author = user.displayName,
            description = food.description,
        )

        return when(user.userType) {
            UserType.BASIC -> {
                addFoodToPendingListUseCase.execute(newFood, imageUri)
            }
            UserType.ADMIN -> {
                addFoodToMainListUseCase.execute(newFood, imageUri)
            }
        }
    }
}
