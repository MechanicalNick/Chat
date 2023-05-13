package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.data.dto.MessageResponse
import io.reactivex.Single

interface ChangeTopicUseCase {
    fun execute(
        messageId: Long,
        newTopic: String
    ): Single<MessageResponse>
}