package com.tinkoff.homework.use_cases

import com.tinkoff.homework.data.dto.ProfileDto
import com.tinkoff.homework.repository.ProfileRepository
import com.tinkoff.homework.use_cases.interfaces.GetProfileUseCase
import io.reactivex.Single

class GetProfileUseCaseImpl(val repository: ProfileRepository) : GetProfileUseCase {
    override fun execute(profileId: Long?): Single<ProfileDto> {
        return repository.getProfile(profileId)
    }
}