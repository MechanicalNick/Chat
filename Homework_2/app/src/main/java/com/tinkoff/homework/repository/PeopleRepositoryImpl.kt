package com.tinkoff.homework.repository

import com.tinkoff.homework.App
import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.data.domain.Status
import com.tinkoff.homework.data.dto.PresencesResponse
import com.tinkoff.homework.repository.interfaces.PeopleRepository
import com.tinkoff.homework.utils.ZulipChatApi
import io.reactivex.Single
import javax.inject.Inject

class PeopleRepositoryImpl: PeopleRepository {
    @Inject
    lateinit var api: ZulipChatApi

    init {
        App.INSTANCE.appComponent.inject(this)
    }

    override fun getAllPresence(): Single<PresencesResponse> {
        return api.getAllPresence()
    }

    override fun getPeoples(): Single<List<People>> {
        return api.getAllPeoples().map { p -> p.peoples }
            .map { peoples ->
                peoples.map { peopleDto ->
                    People(
                        peopleDto.name,
                        peopleDto.email,
                        peopleDto.email,
                        Status.Offline,
                        peopleDto.avatarUrl
                    )
                }
            }
    }
}