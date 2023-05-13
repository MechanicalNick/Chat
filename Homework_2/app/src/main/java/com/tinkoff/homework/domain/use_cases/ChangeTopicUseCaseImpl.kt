package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.domain.repository.MessageRepository
import com.tinkoff.homework.domain.use_cases.interfaces.ChangeTopicUseCase
import io.reactivex.Single

class ChangeTopicUseCaseImpl(
    private val repository: MessageRepository
) : ChangeTopicUseCase {
    override fun execute(messageId: Long, newTopic: String): Single<MessageResponse> {
        return repository.changeTopic(messageId, newTopic)
    }
}