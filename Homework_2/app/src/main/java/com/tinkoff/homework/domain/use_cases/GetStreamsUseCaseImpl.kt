package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.domain.data.Stream
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.domain.repository.StreamRepository
import io.reactivex.Observable
import io.reactivex.Single

class GetStreamsUseCaseImpl(val repository: StreamRepository) : GetStreamsUseCase {
    override fun execute(
        isSubscribed: Boolean,
        query: String
    ): Observable<List<Stream>> {
        return repository.fetchResults(isSubscribed, query)
    }
}