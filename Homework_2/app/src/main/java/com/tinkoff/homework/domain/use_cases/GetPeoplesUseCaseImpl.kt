package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.repository.interfaces.PeopleRepository
import com.tinkoff.homework.domain.use_cases.interfaces.GetPeoplesUseCase
import com.tinkoff.homework.utils.mapper.toStatus
import io.reactivex.Single

class GetPeoplesUseCaseImpl(val repository: PeopleRepository) : GetPeoplesUseCase {
    override fun execute(): Single<List<People>> {
        return Single.zip(
            repository.getPeoples(),
            repository.getAllPresence()
        ) { peoples, presences ->
            val res = peoples
                .map { peopleDto ->
                    with(peopleDto) {
                        status = toStatus(presences.presences[peopleDto.key]?.aggregated?.status)
                    }
                    peopleDto
                }
                .toList()
            res
        }
    }
}