package com.tinkoff.homework.repository.interfaces

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import io.reactivex.Single

interface StreamRepository {
    fun getAll(): Single<List<Stream>>
    fun getSubscriptions(): Single<List<Stream>>
    fun fetchResults(isSubscribed: Boolean, query: String): Single<List<Stream>>
    fun fetchCashedResults(isSubscribed: Boolean): Single<List<Stream>>
}