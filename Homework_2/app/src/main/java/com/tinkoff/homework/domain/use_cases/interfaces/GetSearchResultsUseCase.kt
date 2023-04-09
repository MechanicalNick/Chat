package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.data.domain.MessageModel
import io.reactivex.Single

interface GetSearchResultsUseCase {
    fun execute(query: String, topic: String, streamId: Long): Single<List<MessageModel>>
}