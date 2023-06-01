package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.data.dto.MessageResponse
import io.reactivex.Single

interface AddReactionUseCase {
    fun execute(
        messageId: Long, emojiName: String
    ): Single<MessageResponse>
}