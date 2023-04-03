package com.tinkoff.homework.repository

import com.tinkoff.homework.App
import com.tinkoff.homework.data.Status
import com.tinkoff.homework.data.dto.ProfileDto
import com.tinkoff.homework.utils.ZulipChatApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProfileRepositoryImpl: ProfileRepository {
    @Inject
    lateinit var api: ZulipChatApi

    init {
        App.INSTANCE.appComponent.inject(this)
    }

    override fun getProfile(profileId: Long?): Single<ProfileDto> {
        return  api.getMyInfo().map { ProfileDto(it.userId, it.name, Status.Online, it.avatarUrl) }
    }
}