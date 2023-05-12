package com.tinkoff.homework

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.platform.app.InstrumentationRegistry
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.tinkoff.homework.data.db.LocalDateConverter
import com.tinkoff.homework.screen.ChatScreen
import com.tinkoff.homework.presentation.ReactionHelper
import com.tinkoff.homework.presentation.view.fragment.chat.ChatFragment
import com.tinkoff.homework.presentation.view.fragment.chat.ChatFragment.Companion.ARG_STREAM
import com.tinkoff.homework.presentation.view.fragment.chat.ChatFragment.Companion.ARG_STREAM_ID
import com.tinkoff.homework.presentation.view.fragment.chat.ChatFragment.Companion.ARG_TOPIC
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.util.concurrent.TimeUnit


class ChatTest : TestCase() {
    @get:Rule
    val mockServer = MockWebServer()

    @Before
    fun setUp() {
        val appComponent = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getAppComponent()
        appComponent.apiUrlProvider.url = mockServer.url("/").toString()
    }

    //Отображение списка сообщений
    @Test
    fun showChatMessagesWithLoading_ByDefault() = run {
        val fragmentScreen = arrangeDefaultScreen()

        step("Сообщения загрузились") {
            fragmentScreen.shimmer {
                flakySafely {
                    isGone()
                }
            }
            fragmentScreen.recycler {
                flakySafely {
                    isDisplayed()
                    val messages = 22
                    val dates = 5
                    hasSize(messages + dates)
                    childAt<ChatScreen.CompanionMessageItem>(2) {
                        companionMessage.isDisplayed()
                        companionAvatar.isDisplayed()
                        companionFlexbox.isDisplayed()
                    }
                    lastChild<ChatScreen.MyMessageItem> {
                        myMessage.hasAnyText()
                        myFlexbox.isDisplayed()
                    }
                    firstChild<ChatScreen.DateMessageItem> {
                        chip.isDisplayed()
                    }
                }
            }
        }
    }

    //Получение пустого списка сообщений
    @Test
    fun showChatMessagesWithLoading_Empty() = run {
        val fragmentScreen = arrangeEmptyScreen()

        step("Загрузился пустой список") {
            fragmentScreen.shimmer {
                flakySafely {
                    isDisplayed()
                }
            }
        }
    }

    //Ошибка загрузки сообщений
    @Test
    fun showChatMessages_Error() = run {
        repeat(2) {
            mockServer.enqueue(getErrorResponse())
        }
        launchFragmentInContainer()
        val fragmentScreen = ChatScreen()

        step("Ошибка загрузки с сервера") {
            fragmentScreen.errorText {
                isDisplayed()
                hasAnyText()
            }
            fragmentScreen.retryButton.isDisplayed()
        }
    }

    //Группировка сообщений по датам
    @Test
    fun showChatMessages_GroupedByDate() = run {
        val fragmentScreen = arrangeDefaultScreen()
        val dates = mapOf(
            0 to LocalDate.of(2023, 4, 13),
            3 to LocalDate.of(2023, 4, 17),
            5 to LocalDate.of(2023, 4, 18),
            14 to LocalDate.of(2023, 4, 24),
            19 to LocalDate.of(2023, 4, 25)
        )
        val converter = LocalDateConverter()

        step("Даты отображаются и показывают верные значения ") {
            fragmentScreen.shimmer {
                flakySafely {
                    isGone()
                }
            }

            fragmentScreen.recycler {
                dates.forEach { (position, date) ->
                    this.childAt<ChatScreen.DateMessageItem>(position) {
                        flakySafely {
                            isDisplayed()
                            chip.hasText(converter.localDateToCharSequence(date))
                        }
                    }
                }
            }
        }

    }

    //Сообщение без реакций
    //Есть собственная реакция к сообщению
    //Реакции сторонних пользователей
    @Test
    fun showChatMessages_ShowReactions() = run {
        val fragmentScreen = arrangeDefaultScreen()
        val expectedCompanionReactions = listOf(
            "${ReactionHelper.emojiCodeToString("1f44d")} 3",
            "${ReactionHelper.emojiCodeToString("1f607")} 1"
        )
        val expectedMyReactions = listOf(
            "${ReactionHelper.emojiCodeToString("1f419")} 1"
        )

        step("Отображение сообщения без реакций") {
            fragmentScreen.shimmer {
                flakySafely {
                    isGone()
                }
            }
            fragmentScreen.recycler {
                isDisplayed()
                childAt<ChatScreen.CompanionMessageItem>(1) {
                    companionFlexbox.hasSize(0)
                }
            }
        }
        step("Отображение сообщения со сторонними реакциями") {
            fragmentScreen.recycler {
                childAt<ChatScreen.CompanionMessageItem>(2) {
                    companionFlexbox.isDisplayed()

                    val reactionCount = expectedCompanionReactions.count()
                    val plusViewCount = 1
                    companionFlexbox.hasSize(reactionCount + plusViewCount)

                    expectedCompanionReactions.forEach {
                        companionFlexbox.hasReaction(it)
                    }
                }
            }
        }
        step("Отображение сообщения с собственными реакциями") {
            fragmentScreen.recycler {
                childAt<ChatScreen.MyMessageItem>(25) {
                    myFlexbox.isDisplayed()

                    val otherReactionCount = 1
                    val reactionCount = expectedMyReactions.count() + otherReactionCount
                    val plusViewCount = 1
                    myFlexbox.hasSize(reactionCount + plusViewCount)

                    expectedMyReactions.forEach {
                        myFlexbox.hasReaction(it)
                    }
                }
            }
        }
    }

    //Клик по реакции. Добавить собственную реакцию
    //Клик по реакции. Удалить собственную реакцию
    @Test
    fun showChatMessages_ChangeReactions() = run {
        val fragmentScreen = arrangeDefaultScreen()
        val myReaction = "${ReactionHelper.emojiCodeToString("1f419")} 1"
        val oldOtherReaction = "${ReactionHelper.emojiCodeToString("1f63a")} 1"
        val newOtherReaction = "${ReactionHelper.emojiCodeToString("1f63a")} 2"
        val otherReactionCount = 1
        val myReactionCount = 1
        val plusViewCount = 1

        step("Удаление реакции") {
            fragmentScreen.recycler {
                childAt<ChatScreen.MyMessageItem>(25) {
                    myFlexbox.isDisplayed()
                    myFlexbox.hasSize(myReactionCount + otherReactionCount + plusViewCount)

                    myFlexbox.clickToReaction(myReaction)

                    myFlexbox.hasSize(otherReactionCount + plusViewCount)
                }
            }
        }
        step("Добавление реакции") {
            fragmentScreen.recycler {
                childAt<ChatScreen.MyMessageItem>(25) {
                    val otherReactionCount = 1
                    val plusViewCount = 1
                    myFlexbox.hasSize(otherReactionCount + plusViewCount)
                    myFlexbox.hasReaction(oldOtherReaction)

                    myFlexbox.clickToReaction(oldOtherReaction)

                    myFlexbox.hasSize( otherReactionCount + plusViewCount)
                    myFlexbox.hasReaction(newOtherReaction)
                }
            }
        }
    }

    //Добавление реакции лонг кликом на сообщение
    @Test
    fun showChatMessages_AddLongclickReaction() = run {
        val fragmentScreen = arrangeDefaultScreen()
        val otherReactionCount = 1
        val myReactionCount = 1
        val plusViewCount = 1

        step("Добавление реакции") {
            flakySafely {
                fragmentScreen.recycler.isDisplayed()
            }
            fragmentScreen.recycler {
                childAt<ChatScreen.MyMessageItem>(25) {
                    myFlexbox.hasSize(myReactionCount + otherReactionCount + plusViewCount)

                    longClick()
                    bottomRecyclerView.isDisplayed()

                    bottomRecyclerView.childAt<ChatScreen.ReactionItem>(9){
                        click()
                    }
                    val myNewReactionCount = 1
                    myFlexbox.hasSize(myNewReactionCount + myReactionCount + otherReactionCount + plusViewCount)
                }
            }
        }
    }

    private fun arrangeEmptyScreen(): ChatScreen {
        mockServer.dispatcher = MockRequestDispatcher().apply {
            returnsForPath("/messages?anchor=newest&num_before=50&num_after=0&narrow=%5B%7B%22operator%22%3A%22topic%22%2C%22operand%22%3A%22%D0%BD%D0%BE%D0%B2%D1%8B%D0%B9%20%D1%82%D0%BE%D0%BF%D0%B8%D0%BA%22%7D%2C%7B%22operator%22%3A%22stream%22%2C%22operand%22%3A0%7D%5D&apply_markdown=false") {
                setBody(loadFromAssets("empty_messages_request.json"))
                    .setBodyDelay(300, TimeUnit.MILLISECONDS)
            }
        }
        launchFragmentInContainer()
        return ChatScreen()
    }

    private fun arrangeDefaultScreen(): ChatScreen {
        mockServer.dispatcher = MockRequestDispatcher().apply {
            // delay для отображения состояния shimmer.isVisible
            // TODO попробовать заменить на device.uiDevice.waitForIdle
            returnsForPath("/messages?anchor=newest&num_before=50&num_after=0&narrow=%5B%7B%22operator%22%3A%22topic%22%2C%22operand%22%3A%22%D0%BD%D0%BE%D0%B2%D1%8B%D0%B9%20%D1%82%D0%BE%D0%BF%D0%B8%D0%BA%22%7D%2C%7B%22operator%22%3A%22stream%22%2C%22operand%22%3A0%7D%5D&apply_markdown=false") {
                setBody(loadFromAssets("messages_request.json"))
                    .setBodyDelay(300, TimeUnit.MILLISECONDS)
            }
            returnsForPath("/messages/352588452/reactions?emoji_name=octopus"){
                 setBody(loadFromAssets("reaction_request.json"))
            }
            returnsForPath("/messages/352588452/reactions?emoji_name=smiley_cat"){
                setBody(loadFromAssets("reaction_request.json"))
            }
            returnsForPath("/messages/352588452/reactions?emoji_name=grinning_face_with_smiling_eyes"){
                setBody(loadFromAssets("reaction_request.json"))
            }
            returnsForPath("/messages/352588452/reactions?emoji_name=smile"){
                setBody(loadFromAssets("reaction_request.json"))
            }
        }
        launchFragmentInContainer()
        return ChatScreen()
    }

    private fun getErrorResponse(): MockResponse {
        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(403)
        return mockedResponse
    }

    private fun launchFragmentInContainer() {
        launchFragmentInContainer<ChatFragment>(
            bundleOf(
                ARG_TOPIC to "новый топик",
                ARG_STREAM to "stream",
                ARG_STREAM_ID to 1
            )
        )
    }
}