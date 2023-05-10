package com.tinkoff.homework.data.usecase

import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.Reaction
import com.tinkoff.homework.utils.RxRule
import com.tinkoff.homework.utils.TestObserverFactory
import io.reactivex.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.time.LocalDate

@RunWith(value = Parameterized::class)
class GetMessagesUseCaseTest(
    private val data: List<MessageModel>,
    private val expectedId: List<Long>
) {
    @get:Rule
    val rxRule = RxRule()

    @Test
    fun `usecase get messages when db contains data`() {
       val testObserver = TestObserverFactory.getTestObserver(Single.just(data))

        testObserver.assertValue { messages ->
            Assert.assertEquals(expectedId , messages.map { it.id })
            true
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters()
        fun data(): List<Array<Any>> {
            return listOf(
                // Получение списка сообщений, когда в БД есть данные.
                arrayOf(listOf(
                    createMessage(1, 1, "topic1"),
                    createMessage(2, 2, "topic1"),
                    createMessage(3, 1, "topic1"),
                    createMessage(4, 2, "topic1"),
                    createMessage(5, 1, "topic5")
                ), listOf(1L, 3L)),
                // Получение списка сообщений, при отсутствии информации в БД
                arrayOf(listOf<MessageModel>(), listOf<Long>()),
            )
        }

        private fun createMessage(id: Long, streamId: Long, topic: String) : MessageModel {
            return MessageModel(id,
                senderId = 1,
                senderFullName = "FullName",
                subject = topic,
                streamId = streamId,
                text = "text",
                date = LocalDate.MIN,
                avatarUrl = "avatarUrl",
                reactions = mutableListOf(
                    Reaction("emojiCode", "emojiName", 1)
                )
            )
        }
    }
}
