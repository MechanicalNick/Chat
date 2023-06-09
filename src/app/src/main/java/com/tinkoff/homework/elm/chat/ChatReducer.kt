package com.tinkoff.homework.elm.chat

import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.data.dto.ImageResponse
import com.tinkoff.homework.data.mapper.toImageReference
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.Reaction
import com.tinkoff.homework.elm.ViewState
import com.tinkoff.homework.elm.chat.model.ChatCommand
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
import com.tinkoff.homework.utils.Const
import kotlinx.parcelize.RawValue
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import java.time.LocalDateTime

class ChatReducer(private val credentials: Credentials) :
    DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {
    override fun Result.reduce(event: ChatEvent): Any {
        return when (event) {
            is ChatEvent.Ui.RemoveMessage -> {
                commands { +ChatCommand.RemoveMessage(event.messageId) }
            }

            is ChatEvent.Internal.MessageRemoved -> {
                state {
                    copy(
                        items = items?.let { removeMessage(event.messageId, it) }
                    )
                }
            }

            is ChatEvent.Ui.ShowSnackbar -> {
                effects { +ChatEffect.ShowSnackbar(event.message) }
            }

            is ChatEvent.Ui.Init -> {
                state {
                    copy(
                        isLoading = true,
                        state = ViewState.Loading,
                        items = null,
                        error = null
                    )
                }
            }
            is ChatEvent.Ui.LoadData -> {
                commands { +ChatCommand.LoadData(event.topicName, event.streamId) }
            }
            is ChatEvent.Ui.LoadImage -> {
                commands { +ChatCommand.LoadImage(event.uri, event.topicName, event.streamId) }
            }
            is ChatEvent.Ui.LoadNextPage -> {
                state {
                    copy(
                        isShowProgress = true
                    )
                }
                commands {
                    +ChatCommand.LoadNextPage(
                        state.items?.firstOrNull()?.id ?: 0,
                        event.topicName, event.streamId
                    )
                }
            }

            is ChatEvent.Ui.AddReaction -> {
                commands { +ChatCommand.AddReaction(event.messageId, event.reaction) }
            }

            is ChatEvent.Ui.ChangeReaction -> {
                commands { +ChatCommand.ChangeReaction(event.message, event.reaction) }
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
                        addReaction(
                            credentials,
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
                            credentials,
                            event.reaction,
                            message,
                            event.messageId
                        )
                    },
                    itemsState = !itemsState,
                )
            }
            is ChatEvent.Internal.MessageSent -> {
                state {
                    copy(
                        items = items?.let { concatenate(credentials, it, event) },
                        itemsState = !itemsState,
                        needScroll = true
                    )
                }
            }
            is ChatEvent.Internal.DataLoaded -> {
                state {
                    copy(
                        isLoading = false,
                        state = ViewState.ShowData,
                        items = event.messages,
                        error = null,
                        isShowProgress = false,
                        needScroll = true
                    )
                }
            }
            is ChatEvent.Internal.PageDataLoaded -> {
                state {
                    copy(
                        isLoading = false,
                        state = ViewState.ShowData,
                        items = concatenate(items, event.messages),
                        error = null,
                        isShowProgress = false,
                        needScroll = false
                    )
                }
            }
            is ChatEvent.Internal.ErrorLoading -> state {
                copy(
                    isLoading = false,
                    items = null,
                    error = event.error,
                    state = ViewState.Error,
                    isShowProgress = false
                )
            }

            is ChatEvent.Internal.ImageLoaded ->
                commands {
                    +ChatCommand.SendMessage(
                        event.streamId,
                        event.topicName, buildMessage(event.response)
                    )
                }

            is ChatEvent.Ui.GoToChat ->
                effects { +ChatEffect.GoToChat(event.topicName, event.streamName, event.streamId) }

            is ChatEvent.Ui.ChangeTopic -> {
                commands { +ChatCommand.ChangeTopic(event.messageId, event.newTopic) }
            }

            is ChatEvent.Internal.TopicChanged -> {
                state {
                    copy(
                        items = items?.let { changeTopic(event.messageId, event.newTopic, it) }
                    )
                }
            }

            is ChatEvent.Ui.EditMessage -> {
                commands { +ChatCommand.EditMessage(event.messageId, event.newText) }
            }

            is ChatEvent.Internal.MessageEdited -> {
                state {
                    copy(
                        items = items?.let { replaceMessage(event.messageId, event.newText, it) }
                    )
                }
            }

            is ChatEvent.Internal.TimeLimitError -> {
                effects { +ChatEffect.ShowTimeLimitSnackbar }
            }
            is ChatEvent.Internal.LoadImageError -> {
                effects { +ChatEffect.LoadImageErrorSnackbar }
            }
            is ChatEvent.Internal.ReactionError -> {
                effects { +ChatEffect.ReactionErrorSnackbar }
            }
            is ChatEvent.Internal.SendMessageError -> {
                effects { +ChatEffect.SendMessageErrorSnackbar }
            }
            is ChatEvent.Ui.ScrollToLastElement -> {
                effects { +ChatEffect.ScrollToLastElement }
            }
            is ChatEvent.Internal.DontNeedScroll ->{
                state{
                    copy(
                        needScroll = false
                    )
                }
            }
        }
    }
}

private fun buildMessage(response: ImageResponse): String {
    return "${Const.IMAGE_PREFIX}(${response.uri})"
}

private fun changeTopic(
    idToReplace: Long,
    newTopic: String,
    messages: List<MessageModel>
): List<MessageModel> {
    val message = messages.first { it.id == idToReplace }
    val newList = messages.toMutableList()
    newList.remove(message)
    val newMessage = MessageModel(
        message.id,
        message.senderId,
        message.senderFullName,
        newTopic,
        message.streamId,
        message.text,
        message.dateTime,
        message.avatarUrl,
        message.reactions,
        message.imageUrl
    )
    newList.add(newMessage)
    return newList
}

private fun replaceMessage(
    idToReplace: Long,
    newText: String,
    messages: List<MessageModel>
): List<MessageModel> {
    val message = messages.first { it.id == idToReplace }
    val newList = messages.toMutableList()
    newList.remove(message)
    val newMessage = MessageModel(
        id = message.id,
        senderId = message.senderId,
        senderFullName = message.senderFullName,
        topic = message.topic,
        streamId = message.streamId,
        text = newText,
        dateTime = message.dateTime,
        avatarUrl = message.avatarUrl,
        reactions = message.reactions,
        imageUrl = null
    )
    newList.add(newMessage)
    return newList
}


private fun removeMessage(
    idToRemove: Long,
    messages: List<MessageModel>
): List<MessageModel> {
    val message = messages.first { it.id == idToRemove }
    val newList = messages.toMutableList()
    newList.remove(message)
    return newList
}

private fun concatenate(
    messages: @RawValue List<MessageModel>?,
    newMessages: @RawValue List<MessageModel>,
): List<MessageModel> {
    val list = newMessages.toMutableList()
    messages?.let { list.addAll(it) }
    return list.distinctBy { m-> m.id }
}

private fun concatenate(
    credentials: Credentials,
    messages: @RawValue List<MessageModel>,
    event: ChatEvent.Internal.MessageSent
): List<MessageModel> {
    val message = MessageModel(
        id = event.messageId,
        senderId = credentials.id,
        senderFullName = credentials.fullName,
        topic = event.topic,
        streamId = event.streamId,
        text = event.message,
        dateTime = LocalDateTime.now(),
        avatarUrl = credentials.avatar,
        reactions = mutableListOf(),
        imageUrl = toImageReference(event.message)
    )
    val list = messages.toMutableList()
    list.add(message)
    return list
}

private fun applyReaction(
    value: Reaction,
    old: MessageModel,
    applicableId: Long,
    credentials: Credentials,
    equalsCondition: (Reaction?) -> Boolean,
    action: (MutableList<Reaction>, Reaction) -> Boolean
): MessageModel {
    if (old.id != applicableId)
        return old
    val sameReaction =
        old.reactions.firstOrNull {
          // userId свой, в реакции прихоид старый
          r -> r.userId == credentials.id && r.emojiCode == value.emojiCode
        }
    val myValueFromOther = Reaction(value.emojiCode, value.emojiName, credentials.id)
    if (equalsCondition(sameReaction))
        action(old.reactions, myValueFromOther)
    return old
}

private fun addReaction(
    credentials: Credentials,
    value: Reaction,
    old: MessageModel,
    applicableId: Long
): MessageModel {
    val equalsCondition: (Reaction?) -> Boolean =  {r -> r == null }
    val add: (MutableList<Reaction>, Reaction) -> Boolean = { list, reaction -> list.add(reaction)}
    return applyReaction(value, old, applicableId, credentials, equalsCondition, add)
}

private fun removeReaction(
    credentials: Credentials,
    value: Reaction,
    old: MessageModel,
    applicableId: Long
): MessageModel {
    val equalsCondition: (Reaction?) -> Boolean =  {r -> r != null }
    val remove: (MutableList<Reaction>, Reaction) -> Boolean = { list, reaction ->
        val r = list.find { r -> r.userId == reaction.userId && r.emojiCode == reaction.emojiCode}
        r?.let{list.remove(r) }
        true
    }
    return applyReaction(value, old, applicableId, credentials, equalsCondition, remove)
}
