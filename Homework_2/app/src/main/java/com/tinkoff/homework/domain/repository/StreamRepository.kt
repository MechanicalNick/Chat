package com.tinkoff.homework.domain.repository

import com.tinkoff.homework.data.dto.SubscribeOnStreamResponse
import com.tinkoff.homework.domain.data.Stream
import io.reactivex.Single

interface StreamRepository {
    fun getAll(): Single<List<Stream>>
    fun getSubscriptions(): Single<List<Stream>>
    fun fetchResults(isSubscribed: Boolean, query: String): Single<List<Stream>>
    fun fetchCashedResults(isSubscribed: Boolean): Single<List<Stream>>
    fun subscribeOnStream(streamName: String): Single<SubscribeOnStreamResponse>
}