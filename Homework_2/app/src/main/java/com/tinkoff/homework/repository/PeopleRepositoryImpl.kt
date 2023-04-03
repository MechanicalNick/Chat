package com.tinkoff.homework.repository

import com.tinkoff.homework.data.Status
import com.tinkoff.homework.data.dto.PeopleDto
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class PeopleRepositoryImpl: PeopleRepository {
    override fun getPeoples():  Observable<List<PeopleDto>> =
        Observable.fromCallable {
            IntRange(1, 15)
            .map { PeopleDto("Name${it}", "email${it}@gmail.com)",
                if(it % 2 ==0) Status.Online else Status.Offline)
            }
    }.subscribeOn(Schedulers.io()).delay(6, TimeUnit.SECONDS)
}