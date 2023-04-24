package com.tinkoff.homework.elm.chat

import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.domain.Reaction
import com.tinkoff.homework.elm.chat.model.ChatCommand
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
import com.tinkoff.homework.utils.Const
import kotlinx.parcelize.RawValue
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import java.time.LocalDate

class ChatReducer : DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {
    override fun Result.reduce(event: ChatEvent): Any {
        return when (event) {
            is ChatEvent.Ui.Init -> {
                state {
                    copy(
                        isLoading = true,
                        items = null,
                        error = null
                    )
                }
            }
            is ChatEvent.Ui.LoadCashedData -> {
                state {
                    copy(
                        isLoading = true,
                        items = null,
                        error = null,
                        topicName = event.topicName,
                        streamId = event.streamId
                    )
                }
                commands { +ChatCommand.LoadCashedData(state.topicName, state.streamId) }
            }
            is ChatEvent.Ui.LoadData -> {
                commands { +ChatCommand.LoadData(state.topicName, state.streamId) }
            }
            is ChatEvent.Ui.AddReaction -> {
                commands { +ChatCommand.AddReaction(event.messageId, event.reaction) }
            }
            is ChatEvent.Ui.RemoveReaction -> {
                commands { +ChatCommand.RemoveReaction(event.messageId, event.reaction) }
            }
            is ChatEvent.Ui.SendMessage -> {
                commands { +ChatCommand.SendMessage(event.streamId, event.topic, event.message) }
            }
            is ChatEvent.Internal.ReactionAdded -> state {
                copy(
                    items = items?.map { message ->
                        applyReaction(
                            event.reaction,
                            message,
                            event.messageId
                        )
                    },
                    itemsState = !itemsState,
                )
            }
            is ChatEvent.Internal.ReactionRemoved -> state {
                copy(
                    items = items?.map { message ->
                        removeReaction(
                            event.reaction,
                            message,
                            event.messageId
                        )
                    },
                    itemsState = !itemsState,
                )
            }
            is ChatEvent.Internal.MessageSent -> state {
                copy(
                    items = concatenate(items, event),
                    itemsState = !itemsState,
                )
            }
            is ChatEvent.Internal.DataLoaded -> state {
                copy(
                    isLoading = false,
                    items = event.messages,
                    error = null
                )
            }
            is ChatEvent.Internal.ErrorLoading -> state {
                copy(
                    isLoading = false,
                    items = null,
                    error = event.error
                )
            }
        }
    }
}

private fun concatenate(
    messages: @RawValue List<MessageModel>?,
    event: ChatEvent.Internal.MessageSent
): List<MessageModel> {
    val message = MessageModel(
        id = event.messageId,
        senderId = Const.myId,
        senderFullName = Const.myFullName,
        subject = event.topic,
        streamId = event.streamId,
        text = event.message,
        date = LocalDate.now(),
        avatarUrl = Const.myAvatar,
        reactions = mutableListOf()
    )
    val list = messages!!.toMutableList()
    list.add(message)
    return list
}

private fun applyReaction(value: Reaction, old: MessageModel, applicableId: Long): MessageModel {
    if (old.id != applicableId)
        return old
    val sameReaction = old.reactions.firstOrNull { r -> r.userId == Const.myId }
    if (sameReaction == null)
        old.reactions.add(value)
    return old
}

private fun removeReaction(value: Reaction, old: MessageModel, applicableId: Long): MessageModel {
    if (old.id != applicableId)
        return old
    val sameReaction = old.reactions.firstOrNull { r -> r.userId == value.userId }
    if (sameReaction != null)
        old.reactions.remove(value)
    return old
}
