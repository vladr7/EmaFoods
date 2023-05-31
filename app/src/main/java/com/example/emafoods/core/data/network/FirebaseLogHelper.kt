package com.example.emafoods.core.data.network

import com.example.emafoods.core.domain.network.LogHelper
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class FirebaseLogHelper @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics
): LogHelper {

    override fun log(message: String) {
        firebaseCrashlytics.log(message)
    }

    override fun reportCrash(throwable: Throwable) {
        firebaseCrashlytics.recordException(throwable)
    }
}