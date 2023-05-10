package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.use_cases.interfaces.GetSearchResultsUseCase
import com.tinkoff.homework.data.repository.MessageRepositoryImpl
import io.reactivex.Single

class GetSearchResultsUseCaseImpl(private val repositoryImpl: MessageRepositoryImpl) :
    GetSearchResultsUseCase {
    override fun execute(query: String, topic: String, streamId: Long): Single<List<MessageModel>> {
        return repositoryImpl
            .fetchMessages("newest", 1000, 0, topic, streamId, query)
    }
}