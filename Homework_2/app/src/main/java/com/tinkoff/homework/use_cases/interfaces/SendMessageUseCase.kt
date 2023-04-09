package com.tinkoff.homework.use_cases.interfaces

import com.tinkoff.homework.data.dto.MessageResponse
import io.reactivex.Single

interface SendMessageUseCase {
    fun execute(
        streamId: Long,
        topic: String,
        message: String
    ): Single<MessageResponse>
}