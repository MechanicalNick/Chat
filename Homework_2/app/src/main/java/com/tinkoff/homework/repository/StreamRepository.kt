package com.tinkoff.homework.repository

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.data.dto.StreamResponse2
import io.reactivex.Single

interface StreamRepository {
    fun search(query: String): Single<StreamResponse2>
    fun getAll(): Single<List<Stream>>
    fun getSubscriptions(): Single<List<Stream>>
    fun getTopics(streamId: Long, streamName: String): Single<List<Topic>>
    fun getResults(isSubscribed: Boolean): Single<List<Stream>>
}