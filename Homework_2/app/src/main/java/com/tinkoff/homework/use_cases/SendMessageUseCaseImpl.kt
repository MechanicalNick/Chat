package com.tinkoff.homework.use_cases

import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.repository.MessageRepository
import com.tinkoff.homework.use_cases.interfaces.SendMessageUseCase
import io.reactivex.Single

class SendMessageUseCaseImpl(val repository: MessageRepository) : SendMessageUseCase {
    override fun execute(
        streamId: Long,
        topic: String,
        message: String
    ): Single<MessageResponse> {
        return repository.sendMessage(streamId, topic, message)
    }
}