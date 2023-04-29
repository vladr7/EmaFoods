package com.example.emafoods.di

import android.content.Context
import com.example.emafoods.core.data.database.getFoodDatabase
import com.example.emafoods.core.data.network.DefaultFirebaseService
import com.example.emafoods.core.domain.network.FirebaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseService():
            FirebaseService = DefaultFirebaseService()

    @Singleton
    @Provides
    fun provideFoodDatabase(
        @ApplicationContext context: Context
    ) = getFoodDatabase(context)
}