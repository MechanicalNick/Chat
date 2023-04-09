package com.tinkoff.homework.use_cases.interfaces

import com.tinkoff.homework.data.dto.MessageResponse
import io.reactivex.Single

interface RemoveReactionUseCase {
    fun execute(
        messageId: Long, emojiName: String
    ): Single<MessageResponse>
}