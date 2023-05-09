package com.tinkoff.homework.repository

import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.data.dto.PresencesResponse
import com.tinkoff.homework.repository.interfaces.PeopleRepository
import com.tinkoff.homework.utils.ZulipChatApi
import com.tinkoff.homework.utils.mapper.toDomain
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