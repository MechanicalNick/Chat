package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.repository.interfaces.MessageRepository
import com.tinkoff.homework.domain.use_cases.interfaces.AddReactionUseCase
import io.reactivex.Single

class AddReactionUseCaseImpl(val repository: MessageRepository) : AddReactionUseCase {
    override fun execute(messageId: Long, emojiName: String): Single<MessageResponse> {
        return repository.addReaction(messageId, emojiName)
    }
}