package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.repository.interfaces.StreamRepository
import io.reactivex.Single

class GetStreamsUseCaseImpl(val repository: StreamRepository) : GetStreamsUseCase {
    override fun execute(
        isSubscribed: Boolean,
        isCashed: Boolean,
        query: String
    ): Single<List<Stream>> {
        return if (isCashed) {
            repository.fetchCashedResults(isSubscribed)
        } else {
            repository.fetchResults(isSubscribed, query)
        }
    }
}