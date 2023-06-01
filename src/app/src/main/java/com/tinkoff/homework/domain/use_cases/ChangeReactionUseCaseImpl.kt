package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.MessageResponseWrapper
import com.tinkoff.homework.domain.data.MessageResponseWrapperStatus
import com.tinkoff.homework.domain.use_cases.interfaces.AddReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.ChangeReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveReactionUseCase
import io.reactivex.Single

class ChangeReactionUseCaseImpl(
    private val credentials: Credentials,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase
) : ChangeReactionUseCase {
    override fun execute(message: MessageModel, emojiName: String): Single<MessageResponseWrapper> {
        val currentReaction = message.reactions
            .firstOrNull { r -> r.emojiName == emojiName && r.userId == credentials.id }
        return if (currentReaction == null)
            addReactionUseCase.execute(message.id, emojiName)
                .map { MessageResponseWrapper(it, MessageResponseWrapperStatus.Added) }
        else
            removeReactionUseCase.execute(message.id, emojiName)
                .map { MessageResponseWrapper(it, MessageResponseWrapperStatus.Removed) }
    }
}