package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.data.domain.People
import io.reactivex.Single

interface GetPeoplesUseCase {
    fun execute(): Single<List<People>>
}