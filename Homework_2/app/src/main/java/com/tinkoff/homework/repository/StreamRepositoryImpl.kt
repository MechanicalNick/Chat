package com.tinkoff.homework.repository

import android.util.Log
import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.data.dto.TopicDto
import com.tinkoff.homework.db.dao.StreamDao
import com.tinkoff.homework.repository.interfaces.MessageRepository
import com.tinkoff.homework.repository.interfaces.StreamRepository
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.utils.ZulipChatApi
import com.tinkoff.homework.utils.mapper.toDomainStream
import com.tinkoff.homework.utils.mapper.toStreamEntity
import com.tinkoff.homework.utils.mapper.toTopicEntities
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

    @Inject
    lateinit var streamDao: StreamDao

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


    override fun fetchResults(isSubscribed: Boolean, query: String): Single<List<Stream>> {
        return loadResultsFromServer(isSubscribed)
            .flattenAsObservable { it }
            .filter { stream ->
                if (query.isBlank()) true else stream.name.contains(
                    query,
                    ignoreCase = true
                )
            }
            .toList()
    }

    override fun fetchCashedResults(isSubscribed: Boolean): Single<List<Stream>> {
        return loadLocalResults(isSubscribed)
    }

    private fun loadResultsFromServer(isSubscribed: Boolean): Single<List<Stream>> {
        val collection = if (isSubscribed) getSubscriptions() else getAll()

        val result = collection
            .retryWhen { throwable -> throwable.delay(Const.DELAY, TimeUnit.SECONDS) }
            .flattenAsObservable { it }
            .flatMapSingle { stream ->
                Single.zip(
                    Single.just(stream),
                    getTopics(stream.id, stream.name)
                        .retryWhen { throwable -> throwable.delay(Const.DELAY, TimeUnit.SECONDS) },
                ) { stream, topics ->
                    stream.topics.addAll(topics)
                    stream
                }
            }.toList()

        val subscribe = result
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    refreshLocalDataSource(it, isSubscribed)
                }, {
                    Log.e("error", it.message ?: it.stackTraceToString())
                }
            )

        return result
    }

    private fun loadLocalResults(isSubscribed: Boolean): Single<List<Stream>> {
        val collection =
            if (isSubscribed) streamDao.getSubscribed(onlySubscribed = true) else streamDao.getAll()
        return collection
            .map { list -> list.map { streamResult -> toDomainStream(streamResult) } }
    }

    private fun createTopic(dto: TopicDto, streamId: Long, streamName: String): Single<Topic> {
        return getMessagesByTopic(dto.name, streamId).map { messages ->
            val topic = Topic(dto.name, messages.count().toLong(), streamName, streamId)
            topic
        }
    }

    private fun getMessagesByTopic(topic: String, streamId: Long): Single<List<MessageModel>> {
        return messageRepository.fetchMessages(
            anchor = "newest",
            numBefore = Const.MAX_MESSAGE_COUNT,
            numAfter = 0,
            topic = topic,
            streamId = streamId,
            query = ""
        )
    }

    private fun refreshLocalDataSource(streams: List<Stream>, isSubscribed: Boolean) {
        streams.map {
            streamDao.deleteStreams(isSubscribed)
            if (isSubscribed) {
                streamDao.insertStreamWithReplaceStrategy(
                    toStreamEntity(it, isSubscribed = true),
                    toTopicEntities(it)
                )
            } else {
                streamDao.insertStreamWithIgnoreStrategy(
                    toStreamEntity(it, isSubscribed = false),
                    toTopicEntities(it)
                )
            }
        }
    }
}
