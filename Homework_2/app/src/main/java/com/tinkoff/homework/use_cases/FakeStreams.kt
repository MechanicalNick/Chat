package com.tinkoff.homework.use_cases

import com.tinkoff.homework.data.dto.StreamDto
import com.tinkoff.homework.data.dto.TopicDto

object FakeStreams {
    fun getFakeData(): MutableList<StreamDto> {
        val topics1 = listOf(
            TopicDto(1, "Topic1", 100, "Music"),
            TopicDto(2, "Topic2", 500, "Music"),
            TopicDto(3, "Topic3", 300, "Music"),
            TopicDto(4, "Topic4", 300, "Music"),
            TopicDto(5, "Topic5", 300, "Music"),
            TopicDto(6, "Topic6", 300, "Music"),
            TopicDto(7, "Topic7", 300, "Music"),
            TopicDto(8, "Topic9", 300, "Music"),
            TopicDto(9, "Topic9", 300, "Music"),
        )

        val topics3 = listOf(
            TopicDto(4, "Topic4", 12345, "News")
        )

        return mutableListOf(
            StreamDto(1, "Music", topics1, isSubscribed = true, isExpanded = false),
            StreamDto(2, "Movie", emptyList(), isSubscribed = false, isExpanded = false),
            StreamDto(3, "News", topics3, isSubscribed = false, isExpanded = false)
        )
    }
}