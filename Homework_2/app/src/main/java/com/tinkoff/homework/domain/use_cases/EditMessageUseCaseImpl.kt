package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.domain.repository.MessageRepository
import com.tinkoff.homework.domain.use_cases.interfaces.EditMessageUseCase
import io.reactivex.Single

class EditMessageUseCaseImpl(
    private val repository: MessageRepository
) : EditMessageUseCase {
    override fun execute(messageId: Long, newText: String): Single<MessageResponse> {
        return repository.editMessage(messageId, newText)
    }
}