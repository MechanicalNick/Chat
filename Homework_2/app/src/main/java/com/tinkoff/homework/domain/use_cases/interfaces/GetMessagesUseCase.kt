package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.domain.data.MessageModel
import io.reactivex.Observable

interface GetMessagesUseCase {
    fun execute(
        needClearOld: Boolean,
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long,
        query: String
    ): Observable<List<MessageModel>>
}