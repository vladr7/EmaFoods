package com.example.emafoods.core.domain.localstorage

interface DeviceUtils {

    suspend fun getDeviceUUID(): String
}