package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.MessageResponseWrapper
import io.reactivex.Single

interface ChangeReactionUseCase {
    fun execute(
        message: MessageModel,
        emojiName: String
    ): Single<MessageResponseWrapper>
}