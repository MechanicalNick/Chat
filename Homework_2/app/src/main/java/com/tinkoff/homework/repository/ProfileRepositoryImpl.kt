package com.tinkoff.homework.repository

import com.tinkoff.homework.App
import com.tinkoff.homework.data.domain.Status
import com.tinkoff.homework.data.dto.ProfileDto
import com.tinkoff.homework.repository.interfaces.ProfileRepository
import com.tinkoff.homework.utils.ZulipChatApi
import io.reactivex.Single
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(): ProfileRepository {
    @Inject
    lateinit var api: ZulipChatApi

    override fun getProfile(profileId: Long?): Single<ProfileDto> {
        return  api.getMyInfo().map { ProfileDto(it.userId, it.name, Status.Online, it.avatarUrl) }
    }
}