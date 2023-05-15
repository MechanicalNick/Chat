package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.domain.data.MessageModel
import io.reactivex.Observable
import io.reactivex.Single

interface GetMessagesUseCase {
    fun execute(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long,
        query: String
    ): Observable<List<MessageModel>>
}