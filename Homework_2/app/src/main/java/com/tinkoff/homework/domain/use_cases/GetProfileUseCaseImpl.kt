package com.tinkoff.homework.domain.use_cases

import com.tinkoff.homework.data.domain.Profile
import com.tinkoff.homework.domain.use_cases.interfaces.GetProfileUseCase
import com.tinkoff.homework.repository.interfaces.ProfileRepository
import io.reactivex.Single

class GetProfileUseCaseImpl(val repository: ProfileRepository) : GetProfileUseCase {
    override fun execute(profileId: Long): Single<Profile> {
        return repository.getProfile(profileId)
    }
}