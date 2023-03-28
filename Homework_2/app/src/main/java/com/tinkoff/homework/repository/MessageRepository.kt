package com.tinkoff.homework.repository

import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.data.dto.PeopleDto
import io.reactivex.Observable

interface MessageRepository {
    fun getMessages(): Observable<List<MessageModel>>
}