package com.example.emafoods.core.domain.usecase

import com.example.emafoods.core.domain.repository.FoodRepository
import javax.inject.Inject

class GetAllFoodsUseCase @Inject constructor(
    private val foodsRepository: FoodRepository
) {

    operator fun invoke() =
        foodsRepository.foods
}