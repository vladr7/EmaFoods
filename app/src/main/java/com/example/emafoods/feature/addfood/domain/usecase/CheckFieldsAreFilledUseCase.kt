package com.example.emafoods.feature.addfood.domain.usecase

import javax.inject.Inject

class CheckFieldsAreFilledUseCase @Inject constructor(

) {

    fun execute(foodDescription: String): Boolean {
        if(foodDescription.isEmpty()) {
            return false
        }
        return true
    }
}