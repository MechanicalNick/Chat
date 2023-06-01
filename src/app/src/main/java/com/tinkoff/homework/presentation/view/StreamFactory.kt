package com.tinkoff.homework.presentation.view

import com.tinkoff.homework.domain.data.Stream
import com.tinkoff.homework.domain.data.Topic
import com.tinkoff.homework.presentation.view.adapter.stream.StreamDelegateItem
import com.tinkoff.homework.presentation.view.adapter.topic.TopicDelegateItem
import javax.inject.Inject


class StreamFactory @Inject constructor() {
    val streams = mutableMapOf<Long, Stream>()

    fun updateDelegateItems(streamList: List<Stream>): List<DelegateItem> {
        val delegates = mutableListOf<DelegateItem>()

        var topicId = 1L

        streamList
            .sortedByDescending { stream -> stream.id  }
            .forEach { stream ->
                if(streams.contains(stream.id))
                    streams[stream.id]?.let { stream.isExpanded = it.isExpanded }

                delegates.add(toDelegate(stream))
                if (stream.isExpanded) {
                    stream.topics
                        .sortedByDescending { topic -> topic.name  }
                        .forEach { topic ->
                        delegates.add(toDelegate(topic, topicId++))
                    }
                }
        }

        streams.clear()
        for (s in streamList)
            streams[s.id] = s

        return delegates
    }

    fun updateState(streamId: Long, state: Boolean){
        streams[streamId]?.let { it.isExpanded = state }
    }

    private fun toDelegate(stream: Stream): DelegateItem {
        return StreamDelegateItem(stream.id, stream)
    }

    private fun toDelegate(topic: Topic, topicId: Long): DelegateItem {
        return TopicDelegateItem(topicId, topic)
    }
}