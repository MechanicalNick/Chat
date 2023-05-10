package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import com.tinkoff.homework.domain.repository.MessageRepository
import io.reactivex.Single


class GetMessagesUseCaseImpl(val repository: MessageRepository) : GetMessagesUseCase {
    override fun execute(
        isCashed: Boolean,
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long,
        query: String
    ): Single<List<MessageModel>> {
        return if (isCashed) {
            repository.fetchCashedMessages(streamId, topic)
        } else {
            repository.fetchMessages(
                anchor,
                numBefore,
                numAfter,
                topic,
                streamId,
                query
            )
        }
    }

}