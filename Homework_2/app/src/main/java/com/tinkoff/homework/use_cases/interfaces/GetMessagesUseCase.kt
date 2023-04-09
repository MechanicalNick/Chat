package com.tinkoff.homework.use_cases.interfaces

import com.tinkoff.homework.data.domain.MessageModel
import io.reactivex.Single

interface GetMessagesUseCase {
    fun execute(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long,
        query: String
    ): Single<List<MessageModel>>
}