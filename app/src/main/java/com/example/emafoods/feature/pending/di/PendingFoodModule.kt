package com.example.emafoods.feature.pending.di

import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.core.domain.usecase.RefreshPendingFoodsUseCase
import com.example.emafoods.feature.pending.domain.usecase.DeletePendingFoodUseCase
import com.example.emafoods.feature.pending.domain.usecase.GetAllPendingFoodsUseCase
import com.example.emafoods.feature.pending.domain.usecase.GetFilteredPendingFoodsUseCase
import com.example.emafoods.feature.pending.domain.usecase.MovePendingFoodToAllFoodsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object PendingFoodModule {

    @Provides
    fun provideGetAllPendingFoodsUseCase(
        foodRepository: FoodRepository
    ) = GetAllPendingFoodsUseCase(
        foodRepository = foodRepository
    )

    @Provides
    fun provideRefreshPendingFoodsUseCase(
        foodRepository: FoodRepository
    ) = RefreshPendingFoodsUseCase(
        foodRepository = foodRepository
    )

    @Provides
    fun provideDeletePendingFoodUseCase(
        foodRepository: FoodRepository
    ) = DeletePendingFoodUseCase(
        foodRepository = foodRepository
    )

    @Provides
    fun provideMovePendingFoodToAllFoodsUseCase(
        foodRepository: FoodRepository
    ) = MovePendingFoodToAllFoodsUseCase(
        foodRepository = foodRepository
    )

    @Provides
    fun provideGetFilteredPendingFoodsUseCase(
        getAllPendingFoodsUseCase: GetAllPendingFoodsUseCase,
        getUserDetailsUseCase: GetUserDetailsUseCase
    ) = GetFilteredPendingFoodsUseCase(
        getAllPendingFoodsUseCase = getAllPendingFoodsUseCase,
        getUserDetailsUseCase = getUserDetailsUseCase
    )
}