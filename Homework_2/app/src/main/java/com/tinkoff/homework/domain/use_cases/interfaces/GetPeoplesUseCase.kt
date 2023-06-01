package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.domain.data.People
import io.reactivex.Single

interface GetPeoplesUseCase {
    fun execute(query: String): Single<List<People>>
}