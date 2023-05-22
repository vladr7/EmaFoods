package com.example.emafoods.feature.game.di

import com.example.emafoods.core.data.localstorage.LocalStorage
import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.feature.game.data.datasource.DefaultGameDataSource
import com.example.emafoods.feature.game.data.datasource.GameDataSource
import com.example.emafoods.feature.game.data.repository.DefaultGameRepository
import com.example.emafoods.feature.game.domain.mapper.MapLevelPermissionToViewData
import com.example.emafoods.feature.game.domain.repository.GameRepository
import com.example.emafoods.feature.game.domain.usecase.AddRewardToUserAcceptedRecipeUseCase
import com.example.emafoods.feature.game.domain.usecase.CheckAppOpenedTodayUseCase
import com.example.emafoods.feature.game.domain.usecase.CheckUserLeveledUpUseCase
import com.example.emafoods.feature.game.domain.usecase.GetLevelPermissionsUseCase
import com.example.emafoods.feature.game.domain.usecase.GetListOfXpActionsUseCase
import com.example.emafoods.feature.game.domain.usecase.GetNextLevelUseCase
import com.example.emafoods.feature.game.domain.usecase.GetUnspentUserXpUseCase
import com.example.emafoods.feature.game.domain.usecase.GetUserGameDetailsUseCase
import com.example.emafoods.feature.game.domain.usecase.GetUserRewardsUseCase
import com.example.emafoods.feature.game.domain.usecase.IncreaseXpUseCase
import com.example.emafoods.feature.game.domain.usecase.ResetUnspentUserXpUseCase
import com.example.emafoods.feature.game.domain.usecase.ResetUserRewardsUseCase
import com.example.emafoods.feature.game.domain.usecase.SetAppOpenedTodayUseCase
import com.example.emafoods.feature.game.domain.usecase.StoreUnspentUserXpUseCase
import com.example.emafoods.feature.game.domain.usecase.StoreUserLevelUseCase
import com.example.emafoods.feature.game.domain.usecase.StoreUserXpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameModule {

    @Provides
    @Singleton
    fun provideGameDataSource(
        localStorage: LocalStorage,
        authService: AuthService
    ): GameDataSource = DefaultGameDataSource(
        localStorage = localStorage,
        authService = authService
    )

    @Provides
    @Singleton
    fun provideGameRepository(
        gameDataSource: GameDataSource
    ): GameRepository = DefaultGameRepository(
        gameDataSource = gameDataSource
    )

    @Provides
    @Singleton
    fun provideGetListOfXpActionsUseCase(
        gameRepository: GameRepository
    ) = GetListOfXpActionsUseCase(
        gameRepository = gameRepository
    )

    @Provides
    @Singleton
    fun provideGetLevelPermissionsUseCase(
        gameRepository: GameRepository
    ) = GetLevelPermissionsUseCase(
        gameRepository = gameRepository
    )

    @Provides
    @Singleton
    fun provideGetNextLevelUseCase(
        getUserGameDetailsUseCase: GetUserGameDetailsUseCase
    ) = GetNextLevelUseCase(
        getUserGameDetailsUseCase = getUserGameDetailsUseCase
    )

    @Provides
    @Singleton
    fun provideGetUserGameDetailsUseCase(
        gameRepository: GameRepository
    ) = GetUserGameDetailsUseCase(
        gameRepository
    )

    @Provides
    @Singleton
    fun provideStoreUserLevelUseCase(
        gameRepository: GameRepository
    ) = StoreUserLevelUseCase(
        gameRepository
    )

    @Provides
    @Singleton
    fun provideMapLevelPermissionToViewData(
        getUserGameDetailsUseCase: GetUserGameDetailsUseCase
    ) = MapLevelPermissionToViewData(
        getUserGameDetailsUseCase = getUserGameDetailsUseCase
    )

    @Provides
    @Singleton
    fun provideGetUnspentUserXpUseCase(
        gameRepository: GameRepository
    ) = GetUnspentUserXpUseCase(
        gameRepository
    )

    @Provides
    @Singleton
    fun provideStoreUnspentUserXpUseCase(
        gameRepository: GameRepository
    ) = StoreUnspentUserXpUseCase(
        gameRepository
    )

    @Provides
    @Singleton
    fun provideStoreUserXpUseCase(
        gameRepository: GameRepository
    ) = StoreUserXpUseCase(
        gameRepository
    )

    @Provides
    @Singleton
    fun provideResetUnspentUserXpUseCase(
        gameRepository: GameRepository
    ) = ResetUnspentUserXpUseCase(
        gameRepository
    )

    @Provides
    @Singleton
    fun provideCheckUserLeveledUpUseCase(
        getUserGameDetailsUseCase: GetUserGameDetailsUseCase,
        nextLevelUseCase: GetNextLevelUseCase
    ) = CheckUserLeveledUpUseCase(
        getUserGameDetailsUseCase = getUserGameDetailsUseCase,
        nextLevelUseCase = nextLevelUseCase
    )

    @Provides
    @Singleton
    fun provideCheckAppOpenedTodayUseCase(
        gameRepository: GameRepository
    ) = CheckAppOpenedTodayUseCase(
        gameRepository = gameRepository
    )

    @Provides
    @Singleton
    fun provideSetAppOpenedTodayUseCase(
        gameRepository: GameRepository
    ) = SetAppOpenedTodayUseCase(
        gameRepository = gameRepository
    )

    @Provides
    @Singleton
    fun provideAddRewardToUserAcceptedRecipeUseCase(
        gameRepository: GameRepository
    ) = AddRewardToUserAcceptedRecipeUseCase(
        gameRepository = gameRepository
    )

    @Provides
    @Singleton
    fun provideGetUserRewardsUseCase(
        gameRepository: GameRepository
    ) = GetUserRewardsUseCase(
        gameRepository = gameRepository
    )

    @Provides
    @Singleton
    fun provideResetUserRewardsUseCase(
        gameRepository: GameRepository
    ) = ResetUserRewardsUseCase(
        gameRepository = gameRepository
    )

    @Provides
    @Singleton
    fun provideIncreaseXpUseCase(
        storeUnspentUserXpUseCase: StoreUnspentUserXpUseCase,
        getUnspentUserXpUseCase: GetUnspentUserXpUseCase,
        storeUserXpUseCase: StoreUserXpUseCase,
        resetUnspentUserXpUseCase: ResetUnspentUserXpUseCase,
        checkUserLeveledUpUseCase: CheckUserLeveledUpUseCase,
        storeUserLevelUseCase: StoreUserLevelUseCase,
        nextLevelUseCase: GetNextLevelUseCase
    ) = IncreaseXpUseCase(
        storeUnspentUserXpUseCase = storeUnspentUserXpUseCase,
        getUnspentUserXpUseCase = getUnspentUserXpUseCase,
        storeUserXpUseCase = storeUserXpUseCase,
        resetUnspentUserXpUseCase = resetUnspentUserXpUseCase,
        checkUserLeveledUpUseCase = checkUserLeveledUpUseCase,
        storeUserLevelUseCase = storeUserLevelUseCase,
        nextLevelUseCase = nextLevelUseCase
    )
}