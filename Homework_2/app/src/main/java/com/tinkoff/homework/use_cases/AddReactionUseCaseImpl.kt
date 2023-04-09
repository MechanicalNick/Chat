package com.tinkoff.homework.use_cases

import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.repository.MessageRepository
import com.tinkoff.homework.use_cases.interfaces.AddReactionUseCase
import io.reactivex.Single

class AddReactionUseCaseImpl(val repository: MessageRepository) : AddReactionUseCase {
    override fun execute(messageId: Long, emojiName: String): Single<MessageResponse> {
        return repository.addReaction(messageId, emojiName)
    }
}