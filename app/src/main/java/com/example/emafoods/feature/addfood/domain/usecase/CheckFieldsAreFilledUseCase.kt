package com.example.emafoods.feature.addfood.domain.usecase

import javax.inject.Inject

class CheckFieldsAreFilledUseCase @Inject constructor(

) {

    fun execute(foodTitle: String, foodDescription: String): Boolean {
        if(foodTitle.isEmpty() || foodDescription.isEmpty()) {
            return false
        }
        return true
    }
}