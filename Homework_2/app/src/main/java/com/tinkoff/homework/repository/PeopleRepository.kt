package com.tinkoff.homework.repository

import com.tinkoff.homework.data.dto.PeopleDto
import io.reactivex.Observable

interface PeopleRepository {
    fun getPeoples(): Observable<List<PeopleDto>>
}