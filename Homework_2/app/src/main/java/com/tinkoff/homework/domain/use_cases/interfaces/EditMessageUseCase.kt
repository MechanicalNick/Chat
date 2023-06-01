package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.data.dto.MessageResponse
import io.reactivex.Single

interface EditMessageUseCase {
    fun execute(
        messageId: Long,
        newText: String
    ): Single<MessageResponse>
}