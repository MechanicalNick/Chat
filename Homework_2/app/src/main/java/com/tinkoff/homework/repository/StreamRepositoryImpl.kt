package com.tinkoff.homework.repository

import com.tinkoff.homework.App
import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.data.dto.TopicDto
import com.tinkoff.homework.repository.interfaces.MessageRepository
import com.tinkoff.homework.repository.interfaces.StreamRepository
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.utils.ZulipChatApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class StreamRepositoryImpl @Inject constructor() : StreamRepository {
    @Inject
    lateinit var api: ZulipChatApi

    @Inject
    lateinit var messageRepository: MessageRepository

    override fun getAll(): Single<List<Stream>> {
        return api.getAllStreams()
            .map { response -> response.streams }
            .map { list ->
                list.map { dto ->
                    Stream(
                        dto.streamId, dto.name, mutableListOf(),
                        isExpanded = false
                    )
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getSubscriptions(): Single<List<Stream>> {
        return api.getSubscriptions()
            .map { response -> response.streams }
            .map { list ->
                list.map { dto ->
                    Stream(
                        dto.streamId, dto.name, mutableListOf(),
                        isExpanded = false
                    )
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getTopics(streamId: Long, streamName: String): Single<List<Topic>> {
        return api.getAllTopics(streamId)
            .flatMapObservable { topic -> Observable.fromIterable(topic.topics) }
            .flatMapSingle { topicDto -> createTopic(topicDto, streamId, streamName) }
            .subscribeOn(Schedulers.io())
            .toList()
    }

    override fun getResults(isSubscribed: Boolean, query: String): Single<List<Stream>> {
        val collection = if (isSubscribed) getSubscriptions() else getAll()
        return collection
            .retryWhen { throwable -> throwable.delay(Const.DELAY, TimeUnit.SECONDS)}
            .flattenAsObservable { it }
            .filter { stream ->
                if (query.isBlank()) true else stream.name.contains(query, ignoreCase = true)
            }
            .flatMapSingle { stream ->
                Single.zip(
                    Single.just(stream),
                    getTopics(stream.id, stream.name)
                        .retryWhen { throwable -> throwable.delay(Const.DELAY, TimeUnit.SECONDS)},
                ) { stream, topics ->
                    stream.topics.addAll(topics)
                    stream
                }
            }
            .toList()
    }

    private fun createTopic(dto: TopicDto, streamId: Long, streamName: String): Single<Topic> {
        return getMessagesByTopic(dto.name, streamId).map { messages ->
            val topic = Topic(dto.name, messages.count().toLong(), streamName, streamId)
            topic
        }
    }

    private fun getMessagesByTopic(topic: String, streamId: Long): Single<List<MessageModel>> {
        return messageRepository.getMessages(
            anchor = "newest",
            numBefore = Const.MAX_MESSAGE_COUNT,
            numAfter = 0,
            topic = topic,
            streamId = streamId,
            query = ""
        )
    }
}
