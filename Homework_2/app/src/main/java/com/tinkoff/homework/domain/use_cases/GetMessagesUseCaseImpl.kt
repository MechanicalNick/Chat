package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.repository.interfaces.MessageRepository
import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import io.reactivex.Single


class GetMessagesUseCaseImpl(val repository: MessageRepository) : GetMessagesUseCase {
    override fun execute(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long,
        query: String
    ): Single<List<MessageModel>> {
        return repository.getMessages(
            anchor,
            numBefore,
            numAfter,
            topic,
            streamId,
            query
        )
    }

}