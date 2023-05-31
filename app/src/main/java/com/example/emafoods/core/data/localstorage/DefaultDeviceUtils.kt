package com.example.emafoods.core.data.localstorage

import com.example.emafoods.core.domain.localstorage.DeviceUtils
import com.example.emafoods.core.domain.localstorage.LocalStorage
import javax.inject.Inject

class DefaultDeviceUtils @Inject constructor(
    private val localStorage: LocalStorage
) : DeviceUtils {

    override suspend fun getDeviceUUID(): String {
        val uuid = localStorage.getString(LocalStorageKeys.DEVICE_UUID, null)
        if (uuid == null) {
            val newUUID = java.util.UUID.randomUUID().toString()
            localStorage.putString(LocalStorageKeys.DEVICE_UUID, newUUID)
            return newUUID
        }
        return uuid
    }
}