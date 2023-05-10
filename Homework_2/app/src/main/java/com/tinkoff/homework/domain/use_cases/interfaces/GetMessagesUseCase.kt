package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.domain.data.MessageModel
import io.reactivex.Single

interface GetMessagesUseCase {
    fun execute(
        isCashed: Boolean,
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long,
        query: String
    ): Single<List<MessageModel>>
}