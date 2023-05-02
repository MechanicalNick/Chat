package com.tinkoff.homework.elm.chat

import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendImageUseCase
import com.tinkoff.homework.elm.chat.model.ChatCommand
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.utils.MessageFactory
import io.reactivex.Observable
import vivid.money.elmslie.rx2.Actor

class ChatActor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendImageUseCase: SendImageUseCase,
    private var messageFactory: MessageFactory
) : Actor<ChatCommand, ChatEvent> {
    override fun execute(command: ChatCommand): Observable<ChatEvent> {
        return when (command) {
            is ChatCommand.LoadCashedData -> getMessagesUseCase.execute(
                isCashed = true,
                anchor = "newest", numBefore = Const.MAX_MESSAGE_COUNT_IN_DB,
                numAfter = 0, topic = command.topicName, streamId = command.streamId, query = ""
            )
                .mapEvents(
                    { messages -> ChatEvent.Internal.DataLoaded(messages) },
                    { error -> ChatEvent.Internal.ErrorLoading(error) }
                )
            is ChatCommand.LoadData ->
                getMessagesUseCase.execute(
                    isCashed = false,
                    anchor = "newest", numBefore = Const.MAX_MESSAGE_COUNT_IN_DB,
                    numAfter = 0, topic = command.topicName, streamId = command.streamId, query = ""
                )
                    .mapEvents(
                        { messages -> ChatEvent.Internal.DataLoaded(messages) },
                        { error -> ChatEvent.Internal.ErrorLoading(error) }
                    )

            is ChatCommand.LoadImage ->
                sendImageUseCase.execute(command.uri)
                    .mapEvents(
                        { response -> ChatEvent.Internal.ImageLoaded(response,
                            command.streamId, command.topicName) },
                        { error -> ChatEvent.Internal.ErrorLoading(error) }
                    )

            is ChatCommand.LoadNextPage ->
                getMessagesUseCase.execute(
                    isCashed = false,
                    anchor = command.messageId.toString(), numBefore = Const.MAX_MESSAGE_ON_PAGE,
                    numAfter = 0, topic = command.topicName, streamId = command.streamId, query = ""
                )
                    .mapEvents(
                        { messages -> ChatEvent.Internal.PageDataLoaded(messages) },
                        { error -> ChatEvent.Internal.ErrorLoading(error) }
                    )
            is ChatCommand.AddReaction -> messageFactory.addReaction(
                command.messageId,
                command.reaction
            )
                .mapEvents(
                    { ChatEvent.Internal.ReactionAdded(command.messageId, command.reaction) },
                    { error -> ChatEvent.Internal.ErrorLoading(error) }
                )
            is ChatCommand.RemoveReaction -> messageFactory.removeReaction(
                command.messageId,
                command.reaction
            )
                .mapEvents(
                    { ChatEvent.Internal.ReactionRemoved(command.messageId, command.reaction) },
                    { error -> ChatEvent.Internal.ErrorLoading(error) }
                )
            is ChatCommand.SendMessage -> messageFactory.sendMessage(
                command.streamId,
                command.topic,
                command.message
            )
                .mapEvents(
                    { message ->
                        ChatEvent.Internal.MessageSent(
                            message.id,
                            command.streamId,
                            command.topic,
                            command.message
                        )
                    },
                    { error -> ChatEvent.Internal.ErrorLoading(error) }
                )
        }
    }
}