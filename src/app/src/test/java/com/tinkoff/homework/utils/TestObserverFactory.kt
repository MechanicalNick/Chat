package com.tinkoff.homework.utils

import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.data.stub.MessageRepositoryStub
import com.tinkoff.homework.domain.use_cases.GetMessagesUseCaseImpl
import io.reactivex.Single
import io.reactivex.observers.TestObserver

object TestObserverFactory{
    fun getTestObserver(provider: Single<List<MessageModel>>) : TestObserver<List<MessageModel>> {
        val repository = MessageRepositoryStub().apply { messageProvider = { provider } }
        val useCase = GetMessagesUseCaseImpl(repository = repository)
        return useCase
            .execute(false,
                anchor = "newest",
                numBefore = 1000,
                numAfter = 0,
                topic = "topic1",
                streamId = 1,
                query = ""
            )
            .test()
    }
}