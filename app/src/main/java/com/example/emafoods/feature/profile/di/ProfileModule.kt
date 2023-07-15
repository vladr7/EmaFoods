package com.example.emafoods.feature.profile.di

import com.example.emafoods.core.domain.localstorage.LocalStorage
import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.feature.profile.data.repository.DefaultProfileRepository
import com.example.emafoods.feature.profile.data.repository.datasource.DefaultProfileDataSource
import com.example.emafoods.feature.profile.data.repository.datasource.ProfileDataSource
import com.example.emafoods.feature.profile.domain.repository.ProfileRepository
import com.example.emafoods.feature.profile.domain.usecase.GetNextProfileImageUseCase
import com.example.emafoods.feature.profile.domain.usecase.SignOutUseCase
import com.example.emafoods.feature.profile.domain.usecase.StoreProfileImageChoiceUseCase
import com.example.emafoods.feature.profile.domain.usecase.UpdateUsernameUseCase
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
    fun provideGetNextProfileImageUseCase(
        profileRepository: ProfileRepository,
        logHelper: LogHelper
    ) = GetNextProfileImageUseCase(
        profileRepository = profileRepository,
        logHelper = logHelper
    )

    @Provides
    @Singleton
    fun provideStoreProfileImageChoiceUseCase(
        profileRepository: ProfileRepository
    ) = StoreProfileImageChoiceUseCase(
        profileRepository = profileRepository
    )

    @Provides
    @Singleton
    fun provideProfileRepository(
        localStorage: LocalStorage,
        profileDataSource: ProfileDataSource,
    ): ProfileRepository = DefaultProfileRepository(
        localStorage = localStorage,
        profileDataSource = profileDataSource,
    )

    @Provides
    @Singleton
    fun provideProfileDataSource(
    ): ProfileDataSource = DefaultProfileDataSource()

    @Provides
    @Singleton
    fun provideUpdateUsernameUseCase(
        authService: AuthService
    )= UpdateUsernameUseCase(
        authService = authService
    )
}