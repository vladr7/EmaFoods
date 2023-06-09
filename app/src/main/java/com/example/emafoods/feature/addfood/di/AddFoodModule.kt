package com.example.emafoods.feature.addfood.di

import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.feature.addfood.domain.usecase.AddFoodToMainListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.AddFoodToPendingListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.AddTemporaryPendingImageToRemoteStorageUseCase
import com.example.emafoods.feature.addfood.domain.usecase.CheckFieldsAreFilledUseCase
import com.example.emafoods.feature.addfood.domain.usecase.GetTemporaryPendingImageUseCase
import com.example.emafoods.feature.addfood.domain.usecase.InsertFoodUseCase
import com.example.emafoods.feature.addfood.domain.usecase.MoveTempImageToPendingUseCase
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
        addFoodToPendingListUseCase: AddFoodToPendingListUseCase,
    ) = InsertFoodUseCase(
        getUserDetailsUseCase = getUserDetailsUseCase,
        checkFieldsAreFilledUseCase = checkFieldsAreFilledUseCase,
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
        foodRepository: FoodRepository,
        moveTempImageToPendingUseCase: MoveTempImageToPendingUseCase
    ) = AddFoodToPendingListUseCase(
        foodRepository = foodRepository,
        moveTempImageToPendingUseCase = moveTempImageToPendingUseCase
    )

    @Provides
    fun provideAddTemporaryPendingImageToRemoteStorageUseCase(
        foodRepository: FoodRepository,
        getUserDetailsUseCase: GetUserDetailsUseCase
    ) = AddTemporaryPendingImageToRemoteStorageUseCase(
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

    @Provides
    fun provideMoveTempImageToPendingUseCase(
        foodRepository: FoodRepository,
    ) = MoveTempImageToPendingUseCase(
        foodRepository = foodRepository,
    )
}