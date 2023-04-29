package com.example.emafoods.feature.generatefood.domain

import com.example.emafoods.core.data.models.Food
import javax.inject.Inject

class GenerateFoodUseCase @Inject constructor(

) {

    fun execute(): Food {
        return Food()
    }
}