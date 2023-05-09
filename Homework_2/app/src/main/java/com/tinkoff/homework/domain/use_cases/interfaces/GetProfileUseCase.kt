package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.data.domain.Profile
import io.reactivex.Single

interface GetProfileUseCase {
    fun execute(profileId: Long): Single<Profile>
}