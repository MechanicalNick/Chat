package com.tinkoff.homework.repository

import com.tinkoff.homework.data.dto.StreamRequest
import com.tinkoff.homework.use_cases.GetSearchResultsUseCase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.rx2.rxSingle
import java.util.concurrent.TimeUnit


class StreamRepositoryImpl : StreamRepository {
    override fun search(query: String): Single<StreamRequest> =
        rxSingle {
            GetSearchResultsUseCase.invoke(query)
        }.subscribeOn(Schedulers.io()).delay(6, TimeUnit.SECONDS)

}
