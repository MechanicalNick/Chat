package com.tinkoff.homework.utils

import android.util.Log
import okhttp3.Interceptor
import java.io.IOException
import java.lang.Long.min


class RateLimitInterceptor : Interceptor {
    private val defaultSleepMiils = 1000L
    private var sleepMiils = defaultSleepMiils
    private val maxSleepMills = 120000L // two minutes

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var response = chain.proceed(chain.request())

        if (response.isSuccessful) {
            sleepMiils = defaultSleepMiils
        } else {
            if (response.code == 429) {
                Log.e("rate limit error", response.message)
                System.err.println()
                try {
                    Log.e("rate limit error", "retrying $sleepMiils ms")
                    Thread.sleep(min(sleepMiils, maxSleepMills))
                    sleepMiils *= 2
                } catch (_: InterruptedException) {
                }
                response = chain.proceed(chain.request())
            }
        }

        return response
    }
}