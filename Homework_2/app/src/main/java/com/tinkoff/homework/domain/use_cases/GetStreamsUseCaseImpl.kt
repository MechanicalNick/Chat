package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.repository.interfaces.StreamRepository
import io.reactivex.Single

class GetStreamsUseCaseImpl(val repository: StreamRepository) : GetStreamsUseCase {
    override fun execute(isSubscribed: Boolean, query: String): Single<List<Stream>> {
        return repository.getResults(isSubscribed, query)
    }
}