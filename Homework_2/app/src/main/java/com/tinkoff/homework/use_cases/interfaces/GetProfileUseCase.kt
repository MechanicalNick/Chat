package com.tinkoff.homework.use_cases.interfaces

import com.tinkoff.homework.data.dto.ProfileDto
import io.reactivex.Single

interface GetProfileUseCase {
    fun execute(profileId: Long?): Single<ProfileDto>
}