package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.repository.MessageRepository
import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import io.reactivex.Observable


class GetMessagesUseCaseImpl(val repository: MessageRepository) : GetMessagesUseCase {
    override fun execute(
        needClearOld: Boolean,
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long,
        query: String
    ): Observable<List<MessageModel>> {
        return repository.fetchMessages(
            needClearOld,
            anchor,
            numBefore,
            numAfter,
            topic,
            streamId,
            query
        )
    }

}