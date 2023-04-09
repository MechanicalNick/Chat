package com.tinkoff.homework.use_cases

import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.repository.MessageRepositoryImpl
import com.tinkoff.homework.use_cases.interfaces.GetSearchResultsUseCase
import io.reactivex.Single

class GetSearchResultsUseCaseImpl(private val repositoryImpl: MessageRepositoryImpl) :
    GetSearchResultsUseCase {
    override fun execute(query: String, topic: String, streamId: Long): Single<List<MessageModel>> {
        return repositoryImpl
            .getMessages("newest", 1000, 0, topic, streamId, query)
    }
}