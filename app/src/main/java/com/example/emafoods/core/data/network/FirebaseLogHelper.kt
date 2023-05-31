package com.example.emafoods.core.data.network

import com.example.emafoods.core.domain.constants.AnalyticsConstants
import com.example.emafoods.core.domain.constants.AnalyticsConstants.TIMESTAMP
import com.example.emafoods.core.domain.localstorage.DeviceUtils
import com.example.emafoods.core.domain.network.LogHelper
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class FirebaseLogHelper @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseAuth: FirebaseAuth,
    private val deviceUtils: DeviceUtils
) : LogHelper {

    override suspend fun logUserEvent(eventName: String) {
        val bundle = android.os.Bundle()
        bundle.putString(TIMESTAMP, System.currentTimeMillis().toString())
        bundle.putString(AnalyticsConstants.UUID, deviceUtils.getDeviceUUID())
        bundle.putString(AnalyticsConstants.USER_EMAIL, firebaseAuth.currentUser?.email)
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    override fun logForNextCrash(message: String) {
        firebaseCrashlytics.log(message)
    }

    override fun reportCrash(throwable: Throwable) {
        firebaseCrashlytics.recordException(throwable)
    }
}