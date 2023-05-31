package com.example.emafoods.core.domain.network

interface LogHelper {

    fun log(message: String)
    fun reportCrash(throwable: Throwable)
}