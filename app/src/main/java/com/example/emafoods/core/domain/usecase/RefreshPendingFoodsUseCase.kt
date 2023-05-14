package com.example.emafoods.core.domain.usecase

import com.example.emafoods.core.domain.repository.FoodRepository
import javax.inject.Inject

class RefreshPendingFoodsUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {

    suspend fun execute() {
        foodRepository.refreshPendingFoods()
    }
}