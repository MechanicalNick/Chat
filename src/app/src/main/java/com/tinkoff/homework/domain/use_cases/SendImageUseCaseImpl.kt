package com.tinkoff.homework.domain.use_cases

import android.net.Uri
import com.tinkoff.homework.data.dto.ImageResponse
import com.tinkoff.homework.domain.use_cases.interfaces.SendImageUseCase
import com.tinkoff.homework.domain.repository.MessageRepository
import io.reactivex.Single

class SendImageUseCaseImpl(val repository: MessageRepository): SendImageUseCase {
    override fun execute(
       uri: Uri
    ): Single<ImageResponse> {
        return repository.sendImage(uri)
    }
}