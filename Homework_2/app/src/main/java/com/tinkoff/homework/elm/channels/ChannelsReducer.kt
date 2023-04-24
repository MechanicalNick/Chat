package com.tinkoff.homework.elm.channels

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.elm.channels.model.ChannelsCommand
import com.tinkoff.homework.elm.channels.model.ChannelsEffect
import com.tinkoff.homework.elm.channels.model.ChannelsEvent
import com.tinkoff.homework.elm.channels.model.ChannelsState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ChannelsReducer :
    DslReducer<ChannelsEvent, ChannelsState, ChannelsEffect, ChannelsCommand>() {
    override fun Result.reduce(event: ChannelsEvent): Any {
        return when (event) {
            is ChannelsEvent.Internal.DataLoaded -> state {
                copy(
                    items = event.streams,
                    flagForUpdateUi = !flagForUpdateUi,
                    error = null
                )
            }
            is ChannelsEvent.Internal.ErrorLoading -> state {
                copy(
                    items = null,
                    error = event.error
                )
            }
            is ChannelsEvent.Ui.Wait -> {
                state {
                    copy(
                        items = null,
                        error = null
                    )
                }
            }
            is ChannelsEvent.Ui.LoadCashedData -> {
                state {
                    copy(
                        items = null,
                        error = null
                    )
                }
                commands { +ChannelsCommand.LoadCashedData(state.onlySubscribed) }
            }
            is ChannelsEvent.Ui.LoadData -> {
                commands { +ChannelsCommand.LoadData(state.onlySubscribed) }
            }
            is ChannelsEvent.Ui.Search -> {
                commands { +ChannelsCommand.Search(state.onlySubscribed, event.query) }
            }
            is ChannelsEvent.Ui.CollapseStream -> {
                state {
                    copy(
                        items = items?.map { item -> applyExpanded(false, item, event.stream) },
                        flagForUpdateUi = !flagForUpdateUi
                    )
                }
            }
            is ChannelsEvent.Ui.ExpandStream -> {
                state {
                    copy(
                        items = items?.map { item -> applyExpanded(true, item, event.stream) },
                        flagForUpdateUi = !flagForUpdateUi
                    )
                }
            }
            is ChannelsEvent.Ui.GoToChat -> {
                effects { +ChannelsEffect.GoToChat(event.topicName, event.streamName, event.streamId) }
            }
        }
    }

    private fun applyExpanded(value: Boolean, old: Stream, applicable: Stream): Stream {
        if (applicable != old)
            return old
        applicable.isExpanded = value
        return applicable
    }
}