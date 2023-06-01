package com.tinkoff.homework.data.usecase

import android.accounts.NetworkErrorException
import com.tinkoff.homework.utils.RxRule
import com.tinkoff.homework.utils.TestObserverFactory
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test

class GetMessagesUseCaseErrorTest{
    @get:Rule
    val rxRule = RxRule()

    //Ошибка загрузки данных с сервер
    @Test
    fun `usecase get messages when error`() {
        val testObserver = TestObserverFactory.getTestObserver(Single.error(NetworkErrorException()))

        testObserver.assertError(NetworkErrorException::class.java)
    }
}