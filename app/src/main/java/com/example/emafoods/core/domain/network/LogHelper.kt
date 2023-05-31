package com.example.emafoods.core.domain.network

interface LogHelper {

    suspend fun logUserEvent(eventName: String)
    fun logForNextCrash(message: String)
    fun reportCrash(throwable: Throwable)
}