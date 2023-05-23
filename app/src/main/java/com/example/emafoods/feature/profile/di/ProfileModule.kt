package com.example.emafoods.feature.profile.di

import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.feature.profile.domain.usecase.SignOutUseCase
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


}