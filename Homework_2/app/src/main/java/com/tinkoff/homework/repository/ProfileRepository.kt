package com.tinkoff.homework.repository

import com.tinkoff.homework.data.dto.ProfileDto
import io.reactivex.Single

interface ProfileRepository {
    fun getProfile(profileId: Long?): Single<ProfileDto>
}