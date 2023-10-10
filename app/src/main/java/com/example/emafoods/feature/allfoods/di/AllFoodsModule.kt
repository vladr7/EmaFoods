package com.example.emafoods.feature.allfoods.di

import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.feature.allfoods.domain.usecase.UpdateFoodUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AllFoodsModule {

    @Provides
    fun provideUpdateFoodUseCase(
        foodRepository: FoodRepository
    ) = UpdateFoodUseCase(
        foodRepository = foodRepository
    )
}
