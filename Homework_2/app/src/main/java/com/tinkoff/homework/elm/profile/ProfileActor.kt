package com.tinkoff.homework.elm.profile

import com.tinkoff.homework.domain.use_cases.interfaces.GetProfileUseCase
import com.tinkoff.homework.elm.profile.model.ProfileCommand
import com.tinkoff.homework.elm.profile.model.ProfileEvent
import io.reactivex.Observable
import vivid.money.elmslie.rx2.Actor

class ProfileActor(
    private val profileUseCase: GetProfileUseCase
) : Actor<ProfileCommand, ProfileEvent> {
    override fun execute(command: ProfileCommand): Observable<ProfileEvent> {
        return when (command) {
            is ProfileCommand.LoadData -> profileUseCase.execute(command.profileId)
                .mapEvents(
                    { profileDto -> ProfileEvent.Internal.DataLoaded(profileDto) },
                    { error -> ProfileEvent.Internal.ErrorLoading(error) }
                )
        }
    }
}