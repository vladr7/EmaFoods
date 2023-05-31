package com.example.emafoods.core.di

import android.content.Context
import com.example.emafoods.core.data.database.FoodDatabase
import com.example.emafoods.core.data.database.getFoodDatabase
import com.example.emafoods.core.data.datasource.DefaultFoodDataSource
import com.example.emafoods.core.data.localstorage.DefaultDataStore
import com.example.emafoods.core.data.localstorage.DefaultDeviceUtils
import com.example.emafoods.core.data.network.DefaultAuthService
import com.example.emafoods.core.data.network.FirebaseLogHelper
import com.example.emafoods.core.data.repository.DefaultFoodRepository
import com.example.emafoods.core.data.stringdecoder.UriDecoder
import com.example.emafoods.core.domain.datasource.FoodDataSource
import com.example.emafoods.core.domain.localstorage.DeviceUtils
import com.example.emafoods.core.domain.localstorage.LocalStorage
import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.core.domain.repository.FoodRepository
import com.example.emafoods.core.domain.usecase.GetAllFoodsUseCase
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.core.domain.usecase.RefreshFoodsUseCase
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
    fun provideAuthService():
            AuthService = DefaultAuthService()

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

    @Singleton
    @Provides
    fun bindStringDecoder(): StringDecoder = UriDecoder()

    @Singleton
    @Provides
    fun provideGetUserDetailsUseCase(
        authService: AuthService,
    ) = GetUserDetailsUseCase(
        authService = authService,
    )

    @Singleton
    @Provides
    fun provideLocalStorage(
        @ApplicationContext context: Context
    ): LocalStorage = DefaultDataStore(
        context = context
    )

    @Singleton
    @Provides
    fun provideFirebaseCrashlytics()
            : FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseAnalytics(
        @ApplicationContext
        context: Context
    ): FirebaseAnalytics = FirebaseAnalytics.getInstance(
        context
    )

    @Singleton
    @Provides
    fun provideFirebaseAuth(
    ): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideLogHelper(
        firebaseCrashlytics: FirebaseCrashlytics,
        firebaseAnalytics: FirebaseAnalytics,
        firebaseAuth: FirebaseAuth,
        deviceUtils: DeviceUtils
    ): LogHelper = FirebaseLogHelper(
        firebaseCrashlytics = firebaseCrashlytics,
        firebaseAnalytics = firebaseAnalytics,
        firebaseAuth,
        deviceUtils = deviceUtils
    )

    @Singleton
    @Provides
    fun provideDefaultDeviceUtils(
        localStorage: LocalStorage
    ): DeviceUtils = DefaultDeviceUtils(
        localStorage = localStorage
    )
}