package com.tinkoff.homework.utils

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.utils.adapter.stream.StreamDelegateItem
import com.tinkoff.homework.utils.adapter.topic.TopicDelegateItem
import javax.inject.Inject


class StreamFactory @Inject constructor() {
    val delegates = mutableListOf<DelegateItem>()
    val streams = mutableMapOf<Long, Stream>()

    fun updateDelegateItems(streamList: List<Stream>): List<DelegateItem> {
        delegates.clear()

        var topicId = 1L

        streamList.forEach { stream ->
            if(streams.contains(stream.id))
                streams[stream.id]?.let { stream.isExpanded = it.isExpanded }

            delegates.add(toDelegate(stream))
            if (stream.isExpanded) {
                stream.topics.forEach { topic ->
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