package com.tinkoff.homework.use_cases

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.repository.StreamRepository
import com.tinkoff.homework.use_cases.interfaces.GetStreamsUseCase
import io.reactivex.Single

class GetStreamsUseCaseImpl(val repository: StreamRepository) : GetStreamsUseCase {
    override fun execute(isSubscribed: Boolean): Single<List<Stream>> {
        return repository.getResults(isSubscribed)
    }
}