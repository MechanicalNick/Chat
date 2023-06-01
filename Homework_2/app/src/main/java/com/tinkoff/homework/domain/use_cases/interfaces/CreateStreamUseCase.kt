package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.data.dto.SubscribeOnStreamResponse
import io.reactivex.Single

interface CreateStreamUseCase {
    fun execute(streamName: String): Single<SubscribeOnStreamResponse>
}