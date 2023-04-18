package com.tinkoff.homework.utils

import okhttp3.Interceptor
import java.io.IOException


class RateLimitInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var response = chain.proceed(chain.request())

        if (!response.isSuccessful && response.code == 429) {
            System.err.println(response.message)
            try {
                println("wait and retry...")
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
            }
            response = chain.proceed(chain.request())
        }

        return response
    }
}