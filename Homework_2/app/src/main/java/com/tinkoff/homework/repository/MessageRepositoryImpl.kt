package com.tinkoff.homework.repository

import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.use_cases.FakeMessage
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MessageRepositoryImpl: MessageRepository {
    override fun getMessages(): Observable<List<MessageModel>> =
        Observable.fromCallable {
            FakeMessage.getFakeData()
        }.subscribeOn(Schedulers.io()).delay(6, TimeUnit.SECONDS)
}