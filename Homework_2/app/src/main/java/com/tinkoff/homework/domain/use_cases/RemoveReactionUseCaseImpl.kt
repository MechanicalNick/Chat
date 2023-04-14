package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.repository.interfaces.MessageRepository
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveReactionUseCase
import io.reactivex.Single

class RemoveReactionUseCaseImpl(val repository: MessageRepository) : RemoveReactionUseCase {
    override fun execute(messageId: Long, emojiName: String): Single<MessageResponse> {
        return repository.removeReaction(messageId, emojiName)
    }
}