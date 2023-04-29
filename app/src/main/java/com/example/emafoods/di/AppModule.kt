package com.example.emafoods.di

import com.example.emafoods.core.data.network.DefaultFirebaseService
import com.example.emafoods.core.domain.network.FirebaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseService():
            FirebaseService = DefaultFirebaseService()
}