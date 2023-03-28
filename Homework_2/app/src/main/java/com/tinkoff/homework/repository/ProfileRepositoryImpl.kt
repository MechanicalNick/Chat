package com.tinkoff.homework.repository

import com.tinkoff.homework.data.Status
import com.tinkoff.homework.data.dto.ProfileDto
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ProfileRepositoryImpl: ProfileRepository {
    override fun getProfile(profileId: Int): Single<ProfileDto> {
        return Single.fromCallable {
            ProfileDto(
                profileId, "NAME SURNAME", "description",
                Status.Online
            )
        }.subscribeOn(Schedulers.io()).delay(6, TimeUnit.SECONDS)
    }
}