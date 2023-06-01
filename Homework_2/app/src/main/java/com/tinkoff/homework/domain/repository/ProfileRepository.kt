package com.tinkoff.homework.domain.repository

import com.tinkoff.homework.domain.data.Profile
import io.reactivex.Single

interface ProfileRepository {
    fun getProfile(profileId: Long): Single<Profile>
}