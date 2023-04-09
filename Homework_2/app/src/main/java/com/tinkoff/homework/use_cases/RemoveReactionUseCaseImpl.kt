package com.tinkoff.homework.use_cases

import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.repository.MessageRepository
import com.tinkoff.homework.use_cases.interfaces.RemoveReactionUseCase
import io.reactivex.Single

class RemoveReactionUseCaseImpl(val repository: MessageRepository) : RemoveReactionUseCase {
    override fun execute(messageId: Long, emojiName: String): Single<MessageResponse> {
        return repository.removeReaction(messageId, emojiName)
    }
}