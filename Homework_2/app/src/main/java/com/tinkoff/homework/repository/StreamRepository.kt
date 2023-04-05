package com.tinkoff.homework.repository

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import io.reactivex.Single

interface StreamRepository {
    fun getAll(): Single<List<Stream>>
    fun getSubscriptions(): Single<List<Stream>>
    fun getTopics(streamId: Long, streamName: String): Single<List<Topic>>
    fun getResults(isSubscribed: Boolean): Single<List<Stream>>
}