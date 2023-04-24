package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.data.domain.Stream
import io.reactivex.Single

interface GetStreamsUseCase {
    fun execute(
        isSubscribed: Boolean,
        isCashed: Boolean,
        query: String
    ): Single<List<Stream>>
}