package com.example.emafoods.core.di

import android.content.Context
import com.example.emafoods.core.data.database.FoodDatabase
import com.example.emafoods.core.data.database.getFoodDatabase
import com.example.emafoods.core.data.datasource.DefaultFoodDataSource
import com.example.emafoods.core.data.network.DefaultFirebaseService
import com.example.emafoods.core.data.repository.DefaultFoodRepository
import com.example.emafoods.core.domain.datasource.FoodDataSource
import com.example.emafoods.core.domain.network.FirebaseService
import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.domain.usecase.GetAllFoodsUseCase
import com.example.emafoods.core.domain.usecase.RefreshFoodsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideFirebaseService():
            FirebaseService = DefaultFirebaseService()

    @Singleton
    @Provides
    fun provideFoodDatabase(
        @ApplicationContext context: Context
    ) = getFoodDatabase(context)

    @Provides
    fun provideFoodDataSource(): FoodDataSource = DefaultFoodDataSource()

    @Provides
    fun provideFoodRepository(
        foodDatabase: FoodDatabase,
        foodDataSource: FoodDataSource
    ): FoodRepository = DefaultFoodRepository(
        database = foodDatabase,
        foodDataSource = foodDataSource
    )

    @Provides
    fun provideGetAllFoodsUseCase(
        foodRepository: FoodRepository
    ) = GetAllFoodsUseCase(
        foodRepository = foodRepository
    )

    @Provides
    fun provideRefreshFoodsUseCase(
        foodRepository: FoodRepository
    ) = RefreshFoodsUseCase(
        foodRepository = foodRepository
    )
}