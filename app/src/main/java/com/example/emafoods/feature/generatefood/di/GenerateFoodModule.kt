package com.example.emafoods.feature.generatefood.di

import com.example.emafoods.core.domain.usecase.GetAllFoodsUseCase
import com.example.emafoods.feature.generatefood.domain.usecase.GenerateFoodUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GenerateFoodModule {

    @Provides
    fun provideGenerateFoodUseCase(
        getAllFoodsUseCase: GetAllFoodsUseCase
    ) = GenerateFoodUseCase(
        getAllFoodsUseCase = getAllFoodsUseCase
    )
}