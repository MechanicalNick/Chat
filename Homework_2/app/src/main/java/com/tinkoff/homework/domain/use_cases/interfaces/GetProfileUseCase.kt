package com.tinkoff.homework.domain.use_cases.interfaces

import com.tinkoff.homework.domain.data.Profile
import io.reactivex.Single

interface GetProfileUseCase {
    fun execute(profileId: Long): Single<Profile>
}