package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.domain.data.Stream
import com.tinkoff.homework.domain.repository.StreamRepository
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import io.reactivex.Observable

class GetStreamsUseCaseImpl(val repository: StreamRepository) : GetStreamsUseCase {
    override fun execute(
        isSubscribed: Boolean,
        query: String,
        onlyCashed: Boolean
    ): Observable<List<Stream>> {
        return repository.fetchResults(isSubscribed, query, onlyCashed)
    }
}