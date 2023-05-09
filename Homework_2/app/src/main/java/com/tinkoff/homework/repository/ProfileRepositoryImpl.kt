package com.tinkoff.homework.repository

import com.tinkoff.homework.data.domain.Profile
import com.tinkoff.homework.repository.interfaces.ProfileRepository
import com.tinkoff.homework.utils.ZulipChatApi
import com.tinkoff.homework.utils.mapper.toDomainProfile
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ZulipChatApi
) : ProfileRepository {

    override fun getProfile(profileId: Long): Single<Profile> {
        return Single.zip(
            api.getUserInfo(profileId),
            api.getPresence(profileId)
        )
        { userInfo, presence ->
            userInfo.user.toDomainProfile(presence.presence.aggregated.status)
        }   .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}