package com.tinkoff.homework.elm.chat

import com.tinkoff.homework.domain.data.MessageResponseWrapperStatus
import com.tinkoff.homework.domain.use_cases.interfaces.AddReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.ChangeReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.ChangeTopicUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.EditMessageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveMessageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendImageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendMessageUseCase
import com.tinkoff.homework.elm.chat.model.ChatCommand
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.utils.Const
import io.reactivex.Observable
import vivid.money.elmslie.rx2.Actor


class ChatActor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendImageUseCase: SendImageUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val changeReactionUseCase: ChangeReactionUseCase,
    private val removeMessageUseCase: RemoveMessageUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    private val changeTopicUseCase: ChangeTopicUseCase
) : Actor<ChatCommand, ChatEvent> {
    override fun execute(command: ChatCommand): Observable<ChatEvent> {
        return when (command) {
            is ChatCommand.RemoveMessage -> removeMessageUseCase.execute(
                command.messageId
            )
                .mapEvents(
                    { _ -> ChatEvent.Internal.MessageRemoved(command.messageId) },
                    { error -> ChatEvent.Internal.ErrorLoading(error) }
                )

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

            is ChatCommand.AddReaction -> addReactionUseCase.execute(
                command.messageId,
                command.reaction.emojiName
            )
                .mapEvents(
                    { ChatEvent.Internal.ReactionAdded(command.messageId, command.reaction) },
                    { error -> ChatEvent.Internal.ErrorLoading(error) }
                )

            is ChatCommand.ChangeReaction -> changeReactionUseCase.execute(
                command.message,
                command.reaction.emojiName
            )
                .mapEvents(
                    {
                        if (it.status == MessageResponseWrapperStatus.Added)
                            ChatEvent.Internal.ReactionAdded(command.message.id, command.reaction)
                        else
                            ChatEvent.Internal.ReactionRemoved(command.message.id, command.reaction)
                    },
                    { error -> ChatEvent.Internal.ErrorLoading(error) }
                )

            is ChatCommand.RemoveReaction -> removeReactionUseCase.execute(
                command.messageId,
                command.reaction.emojiName
            )
                .mapEvents(
                    { ChatEvent.Internal.ReactionRemoved(command.messageId, command.reaction) },
                    { error -> ChatEvent.Internal.ErrorLoading(error) }
                )

            is ChatCommand.SendMessage -> sendMessageUseCase.execute(
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

            is ChatCommand.EditMessage -> editMessageUseCase.execute(
                command.messageId,
                command.newText
            ).mapEvents(
                {
                    ChatEvent.Internal.MessageEdited(
                        command.messageId,
                        command.newText
                    )
                },
                { error -> ChatEvent.Internal.TimeLimitError(error) }
            )

            is ChatCommand.ChangeTopic -> changeTopicUseCase.execute(
                command.messageId,
                command.topicName
            ).mapEvents(
                {
                    ChatEvent.Internal.TopicChanged(
                        command.messageId,
                        command.topicName
                    )
                },
                { error -> ChatEvent.Internal.TimeLimitError(error) }
            )
        }
    }
}