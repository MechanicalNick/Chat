package com.tinkoff.homework.repository.interfaces

import com.tinkoff.homework.data.domain.Profile
import io.reactivex.Single

interface ProfileRepository {
    fun getProfile(profileId: Long): Single<Profile>
}