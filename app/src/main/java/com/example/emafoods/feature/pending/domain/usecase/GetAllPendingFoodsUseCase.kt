package com.example.emafoods.feature.pending.domain.usecase

import com.example.emafoods.core.domain.repository.FoodRepository
import javax.inject.Inject

class GetAllPendingFoodsUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {

    fun execute() = foodRepository.pendingFoods
}