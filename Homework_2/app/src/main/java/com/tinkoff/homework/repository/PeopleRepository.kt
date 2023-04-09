package com.tinkoff.homework.repository

import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.data.dto.PresencesResponse
import io.reactivex.Single

interface PeopleRepository {
    fun getPeoples(): Single<List<People>>
    fun getAllPresence(): Single<PresencesResponse>
}