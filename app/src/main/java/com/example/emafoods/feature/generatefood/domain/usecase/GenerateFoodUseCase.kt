package com.example.emafoods.feature.generatefood.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.usecase.GetAllFoodsUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GenerateFoodUseCase @Inject constructor(
    private val getAllFoodsUseCase: GetAllFoodsUseCase
) {

    suspend fun execute(): Food {
        val foods = getAllFoodsUseCase.execute().first()
        if(foods.isEmpty()) return Food(id = "empty")
        return foods.random()
    }
}