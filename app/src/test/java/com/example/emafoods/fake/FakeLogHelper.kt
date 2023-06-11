package com.example.emafoods.fake

import com.example.emafoods.core.domain.network.LogHelper

class FakeLogHelper: LogHelper {

    override suspend fun log(eventName: String) {}

    override fun reportCrash(throwable: Throwable) {}
}