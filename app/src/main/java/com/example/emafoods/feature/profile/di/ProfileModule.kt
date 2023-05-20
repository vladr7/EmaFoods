package com.example.emafoods.feature.profile.di

import com.example.emafoods.core.data.localstorage.LocalStorage
import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.feature.profile.data.datasource.DefaultGameDataSource
import com.example.emafoods.feature.profile.data.datasource.GameDataSource
import com.example.emafoods.feature.profile.data.repository.DefaultGameRepository
import com.example.emafoods.feature.profile.domain.mapper.MapLevelPermissionToViewData
import com.example.emafoods.feature.profile.domain.repository.GameRepository
import com.example.emafoods.feature.profile.domain.usecase.GetLevelPermissionsUseCase
import com.example.emafoods.feature.profile.domain.usecase.GetListOfXpActionsUseCase
import com.example.emafoods.feature.profile.domain.usecase.GetNextLevelUseCase
import com.example.emafoods.feature.profile.domain.usecase.GetUserGameDetailsUseCase
import com.example.emafoods.feature.profile.domain.usecase.SignOutUseCase
import com.example.emafoods.feature.profile.domain.usecase.StoreUserLevelUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideSignOutUseCase(
        authService: AuthService
    ) = SignOutUseCase(
        authService = authService
    )

    @Provides
    @Singleton
    fun provideGameDataSource(
        localStorage: LocalStorage
    ): GameDataSource = DefaultGameDataSource(
        localStorage = localStorage
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
}