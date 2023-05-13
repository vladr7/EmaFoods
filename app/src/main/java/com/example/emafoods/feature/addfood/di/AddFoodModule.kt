package com.example.emafoods.feature.addfood.di

import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.feature.addfood.domain.usecase.InsertFoodUseCase
import com.example.emafoods.feature.addfood.domain.usecase.CheckFieldsAreFilledUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AddFoodModule {

    @Provides
    fun provideCheckFieldsAreFilledUseCase() = CheckFieldsAreFilledUseCase()

    @Provides
    fun provideAddFoodUseCase(
        foodRepository: FoodRepository,
        checkFieldsAreFilledUseCase: CheckFieldsAreFilledUseCase
    ) = InsertFoodUseCase(
        foodRepository = foodRepository,
        checkFieldsAreFilledUseCase = checkFieldsAreFilledUseCase
    )
}