package com.example.emafoods.core.domain.usecase

import javax.inject.Inject

class RefreshAllFoodsUseCase @Inject constructor(
    private val refreshFoodsUseCase: RefreshFoodsUseCase,
    private val refreshPendingFoodsUseCase: RefreshPendingFoodsUseCase
) {

    suspend fun execute() {
        refreshFoodsUseCase.execute()
    }
}