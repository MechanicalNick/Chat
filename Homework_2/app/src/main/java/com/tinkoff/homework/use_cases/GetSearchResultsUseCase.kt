package com.tinkoff.homework.use_cases

import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.repository.MessageRepositoryImpl
import io.reactivex.Single

class GetSearchResultsUseCase(private val repositoryImpl: MessageRepositoryImpl) {
    fun invoke(query: String, topic: String, streamId: Long): Single<List<MessageModel>> {
        return repositoryImpl
            .getMessages("newest", 1000, 0, topic, streamId, query)
    }
}