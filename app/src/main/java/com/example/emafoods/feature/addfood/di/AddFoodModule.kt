package com.example.emafoods.feature.addfood.di

import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.feature.addfood.domain.usecase.AddFoodToMainListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.AddFoodToPendingListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.AddPendingImageToTemporaryRemoteStorageUseCase
import com.example.emafoods.feature.addfood.domain.usecase.CheckFieldsAreFilledUseCase
import com.example.emafoods.feature.addfood.domain.usecase.GetTemporaryPendingImageUseCase
import com.example.emafoods.feature.addfood.domain.usecase.InsertFoodUseCase
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
        getUserDetailsUseCase: GetUserDetailsUseCase,
        checkFieldsAreFilledUseCase: CheckFieldsAreFilledUseCase,
        addFoodToMainListUseCase: AddFoodToMainListUseCase,
        addFoodToPendingListUseCase: AddFoodToPendingListUseCase,
    ) = InsertFoodUseCase(
        getUserDetailsUseCase = getUserDetailsUseCase,
        checkFieldsAreFilledUseCase = checkFieldsAreFilledUseCase,
        addFoodToMainListUseCase = addFoodToMainListUseCase,
        addFoodToPendingListUseCase = addFoodToPendingListUseCase,
    )

    @Provides
    fun provideAddFoodToMainListUseCase(
        foodRepository: FoodRepository
    ) = AddFoodToMainListUseCase(
        foodRepository = foodRepository
    )

    @Provides
    fun provideAddFoodToPendingListUseCase(
        foodRepository: FoodRepository
    ) = AddFoodToPendingListUseCase(
        foodRepository = foodRepository
    )

    @Provides
    fun provideAddPendingImageToTemporaryRemoteStorageUseCase(
        foodRepository: FoodRepository,
        getUserDetailsUseCase: GetUserDetailsUseCase
    ) = AddPendingImageToTemporaryRemoteStorageUseCase(
        foodRepository = foodRepository,
        getUserDetailsUseCase = getUserDetailsUseCase
    )

    @Provides
    fun provideGetTemporaryPendingImageUseCase(
        foodRepository: FoodRepository,
        getUserDetailsUseCase: GetUserDetailsUseCase
    ) = GetTemporaryPendingImageUseCase(
        foodRepository = foodRepository,
        getUserDetailsUseCase = getUserDetailsUseCase
    )
}