package com.tinkoff.homework.utils

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.utils.adapter.stream.StreamDelegateItem
import com.tinkoff.homework.utils.adapter.topic.TopicDelegateItem


class StreamFactory {
    val delegates = mutableListOf<DelegateItem>()

    fun updateDelegateItems(streamList: List<Stream>): List<DelegateItem> {
        delegates.clear()

        var topicId = 1L

        streamList.forEach { stream ->
            delegates.add(toDelegate(stream))
            if (stream.isExpanded) {
                stream.topics.forEach { topic ->
                    delegates.add(toDelegate(topic, topicId++))
                }
            }
        }

        return delegates
    }

    private fun toDelegate(stream: Stream): DelegateItem {
        return StreamDelegateItem(stream.id, stream)
    }

    fun toDelegate(topic: Topic, topicId: Long): DelegateItem {
        return TopicDelegateItem(topicId, topic)
    }
}