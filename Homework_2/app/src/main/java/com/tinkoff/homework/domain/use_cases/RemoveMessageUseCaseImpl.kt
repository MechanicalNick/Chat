package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.domain.repository.MessageRepository
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveMessageUseCase
import io.reactivex.Single

class RemoveMessageUseCaseImpl(
    private val repository: MessageRepository
) : RemoveMessageUseCase {
    override fun execute(messageId: Long): Single<MessageResponse> {
        return repository.removeMessage(messageId)
    }
}