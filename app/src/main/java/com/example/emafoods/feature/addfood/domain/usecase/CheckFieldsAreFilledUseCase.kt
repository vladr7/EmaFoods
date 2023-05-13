package com.example.emafoods.feature.addfood.domain.usecase

import javax.inject.Inject

class CheckFieldsAreFilledUseCase @Inject constructor(

) {

    fun execute(foodDescription: String): Boolean {
        if(foodDescription.length < 10) {
            return false
        }
        return true
    }
}