package com.tinkoff.homework.elm.channels

import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.elm.channels.model.ChannelsCommand
import com.tinkoff.homework.elm.channels.model.ChannelsEvent
import io.reactivex.Observable
import vivid.money.elmslie.rx2.Actor

class ChannelsActor(
    private val getStreamsUseCase: GetStreamsUseCase
) : Actor<ChannelsCommand, ChannelsEvent> {
    override fun execute(command: ChannelsCommand): Observable<ChannelsEvent> {
        return when (command) {
            is ChannelsCommand.LoadData -> getStreamsUseCase.execute(
                isSubscribed = command.isSubscribed,
                isCashed = false,
                query = ""

            ).mapEvents(
                { streams -> ChannelsEvent.Internal.DataLoaded(streams) },
                { error -> ChannelsEvent.Internal.ErrorLoading(error) }
            )
            is ChannelsCommand.Search -> getStreamsUseCase.execute(
                isSubscribed = command.isSubscribed,
                isCashed = false,
                query = command.query
            ).mapEvents(
                { streams -> ChannelsEvent.Internal.DataLoaded(streams) },
                { error -> ChannelsEvent.Internal.ErrorLoading(error) }
            )
            is ChannelsCommand.LoadCashedData -> getStreamsUseCase.execute(
                isSubscribed = command.isSubscribed,
                isCashed = true,
                query = ""
            ).mapEvents(
                { streams -> ChannelsEvent.Internal.DataLoaded(streams) },
                { error -> ChannelsEvent.Internal.ErrorLoading(error) }
            )
        }
    }
}