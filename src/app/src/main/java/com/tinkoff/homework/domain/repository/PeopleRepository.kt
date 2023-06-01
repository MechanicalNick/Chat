package com.tinkoff.homework.domain.repository

import com.tinkoff.homework.domain.data.People
import com.tinkoff.homework.data.dto.PresencesResponse
import io.reactivex.Single

interface PeopleRepository {
    fun getPeoples(): Single<List<People>>
    fun getAllPresence(): Single<PresencesResponse>
}