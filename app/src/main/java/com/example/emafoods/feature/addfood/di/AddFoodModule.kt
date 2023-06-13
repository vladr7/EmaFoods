package com.example.emafoods.feature.addfood.di

import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.feature.addfood.domain.usecase.AddFoodToMainListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.AddFoodToPendingListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.AddIngredientToListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.AddTemporaryPendingImageToRemoteStorageUseCase
import com.example.emafoods.feature.addfood.domain.usecase.CheckFieldsAreFilledUseCase
import com.example.emafoods.feature.addfood.domain.usecase.DeserializeIngredientsUseCase
import com.example.emafoods.feature.addfood.domain.usecase.GetTemporaryPendingImageUseCase
import com.example.emafoods.feature.addfood.domain.usecase.InsertFoodUseCase
import com.example.emafoods.feature.addfood.domain.usecase.MoveTempImageToPendingUseCase
import com.example.emafoods.feature.addfood.domain.usecase.RemoveIngredientFromListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.SaveChangedIngredientFromListUseCase
import com.example.emafoods.feature.addfood.domain.usecase.SerializeIngredientsUseCase
import com.example.emafoods.feature.addfood.domain.usecase.UpdateIngredientFocusUseCase
import com.example.emafoods.feature.addfood.presentation.ingredients.models.IngredientMapper
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
        serializeIngredientsUseCase: SerializeIngredientsUseCase,
        ingredientMapper: IngredientMapper
    ) = InsertFoodUseCase(
        getUserDetailsUseCase = getUserDetailsUseCase,
        checkFieldsAreFilledUseCase = checkFieldsAreFilledUseCase,
        addFoodToPendingListUseCase = addFoodToPendingListUseCase,
        serializeIngredientsUseCase = serializeIngredientsUseCase,
        ingredientsMapper = ingredientMapper
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

    @Provides
    fun provideSerializeIngredientsUseCase(
        logHelper: LogHelper
    ) = SerializeIngredientsUseCase(
        logHelper = logHelper
    )

    @Provides
    fun provideDeserializeIngredientsUseCase(
        logHelper: LogHelper
    ) = DeserializeIngredientsUseCase(
        logHelper = logHelper
    )

    @Provides
    fun provideAddIngredientToListUseCase(
    ) = AddIngredientToListUseCase(
    )

    @Provides
    fun provideRemoveIngredientFromListUseCase(
    ) = RemoveIngredientFromListUseCase(
    )

    @Provides
    fun provideSaveChangedIngredientFromListUseCase(
    ) = SaveChangedIngredientFromListUseCase(
    )

    @Provides
    fun provideUpdateIngredientFocusUseCase(
    ) = UpdateIngredientFocusUseCase(
    )
}