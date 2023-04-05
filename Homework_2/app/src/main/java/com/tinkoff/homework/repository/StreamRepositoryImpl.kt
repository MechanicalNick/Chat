package com.tinkoff.homework.repository

import com.tinkoff.homework.App
import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.utils.ZulipChatApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class StreamRepositoryImpl : StreamRepository {
    @Inject
    lateinit var api: ZulipChatApi

    init {
        App.INSTANCE.appComponent.inject(this)
    }

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
            .map { response -> response.topics }
            .map { list -> list.map { dto -> Topic(dto.name, dto.maxId, streamName, streamId) } }
            .subscribeOn(Schedulers.io())
    }

    override fun getResults(isSubscribed: Boolean): Single<List<Stream>> {
        val collection = if (isSubscribed) getSubscriptions() else getAll()
        return collection
            .flattenAsObservable { it }
            .flatMapSingle { stream ->
                Single.zip(
                    Single.just(stream),
                    getTopics(stream.id, stream.name),
                ) { stream, topics ->
                    stream.topics.addAll(topics)
                    stream
                }
            }
            .toList()
    }
}
