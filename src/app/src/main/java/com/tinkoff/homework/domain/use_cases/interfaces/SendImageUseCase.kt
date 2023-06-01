package com.tinkoff.homework.domain.use_cases.interfaces

import android.net.Uri
import com.tinkoff.homework.data.dto.ImageResponse
import io.reactivex.Single

interface SendImageUseCase {
    fun execute(uri: Uri): Single<ImageResponse>
}