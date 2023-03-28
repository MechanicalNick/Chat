package com.tinkoff.homework.repository

import com.tinkoff.homework.data.dto.StreamRequest
import io.reactivex.Single

interface StreamRepository {
    fun search(query: String): Single<StreamRequest>
}