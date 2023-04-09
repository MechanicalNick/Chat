package com.tinkoff.homework.use_cases.interfaces

import com.tinkoff.homework.data.domain.Stream
import io.reactivex.Single

interface GetStreamsUseCase {
    fun execute(
        isSubscribed: Boolean
    ): Single<List<Stream>>
}