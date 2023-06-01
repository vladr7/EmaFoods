package com.example.emafoods.core.domain.network

interface LogHelper {

    suspend fun log(eventName: String)
    fun reportCrash(throwable: Throwable)
}