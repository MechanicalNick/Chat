package com.tinkoff.homework.data.repository

import com.tinkoff.homework.domain.data.People
import com.tinkoff.homework.data.dto.PresencesResponse
import com.tinkoff.homework.domain.repository.PeopleRepository
import com.tinkoff.homework.data.ZulipChatApi
import com.tinkoff.homework.data.mapper.toDomain
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val api: ZulipChatApi
) : PeopleRepository {

    override fun getAllPresence(): Single<PresencesResponse> {
        return api.getAllPresence()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    override fun getPeoples(): Single<List<People>> {
        return api.getAllPeoples()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { p -> p.peoples }
            .map { peoples ->
                peoples
                    .filter { peopleDto -> !peopleDto.is_bot }
                    .map { peopleDto -> peopleDto.toDomain() }
            }
    }
}