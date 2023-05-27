package com.example.emafoods.feature.pending.domain.usecase

import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFilteredPendingFoodsUseCase @Inject constructor(
    private val getAllPendingFoodsUseCase: GetAllPendingFoodsUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase
) {

    fun execute() : Flow<List<Food>> {
        val userUid = getUserDetailsUseCase.execute().uid
        return getAllPendingFoodsUseCase.execute().map { foods ->
            foods.filter { it.authorUid != userUid }
        }
    }
}