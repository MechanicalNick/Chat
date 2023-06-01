package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.domain.data.People
import com.tinkoff.homework.domain.repository.PeopleRepository
import com.tinkoff.homework.domain.use_cases.interfaces.GetPeoplesUseCase
import com.tinkoff.homework.data.mapper.toStatus
import io.reactivex.Single

class GetPeoplesUseCaseImpl(val repository: PeopleRepository) : GetPeoplesUseCase {
    override fun execute(query: String): Single<List<People>> {
        return Single.zip(
            repository.getPeoples(),
            repository.getAllPresence()
        ) { peoples, presences ->
            val res = peoples
                .filter { people -> filter(people, query) }
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

    fun filter(people: People, query: String) :Boolean{
        if(query.isBlank())
            return true
        return people.name.contains(query, ignoreCase = true) ||
                people.email.contains(query, ignoreCase = true)
    }
}