package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.dto.SubscribeOnStreamResponse
import com.tinkoff.homework.domain.data.Stream
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.domain.repository.StreamRepository
import com.tinkoff.homework.domain.use_cases.interfaces.CreateStreamUseCase
import io.reactivex.Single

class CreateStreamUseCaseImpl(val repository: StreamRepository) : CreateStreamUseCase {
    override fun execute(
        streamName: String
    ): Single<SubscribeOnStreamResponse> {
        return repository.subscribeOnStream(streamName)
    }
}