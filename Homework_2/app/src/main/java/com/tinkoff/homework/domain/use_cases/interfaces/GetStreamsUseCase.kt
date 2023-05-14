package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.domain.data.Stream
import io.reactivex.Observable
import io.reactivex.Single

interface GetStreamsUseCase {
    fun execute(
        isSubscribed: Boolean,
        query: String
    ): Observable<List<Stream>>
}