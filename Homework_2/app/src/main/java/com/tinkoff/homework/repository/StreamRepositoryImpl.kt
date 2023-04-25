package com.tinkoff.homework.repository

import android.util.Log
import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.db.dao.StreamDao
import com.tinkoff.homework.repository.interfaces.MessageRepository
import com.tinkoff.homework.repository.interfaces.StreamRepository
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.utils.ZulipChatApi
import com.tinkoff.homework.utils.mapper.toDomainStream
import com.tinkoff.homework.utils.mapper.toStreamEntity
import com.tinkoff.homework.utils.mapper.toTopicEntities
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class StreamRepositoryImpl @Inject constructor(
    private val api: ZulipChatApi,
    private val messageRepository: MessageRepository,
    private val streamDao: StreamDao
) : StreamRepository {

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
                    api.getAllTopics(stream.id).subscribeOn(Schedulers.io()),
                    fetchMessages()
                ) { stream, topics, messages ->
                    val newTopics = topics.topics.map { topicDto ->
                        Topic(topicDto.name, messages.count { messageModel ->
                            messageModel.subject == topicDto.name && messageModel.streamId == stream.id
                        }.toLong(), stream.name, stream.id)
                    }
                    stream.topics.addAll(newTopics)
                    stream
                }
            }
            .subscribeOn(Schedulers.io())
            .toList()

        val subscribe = result
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
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

    private fun fetchMessages(): Single<List<MessageModel>> {
        return messageRepository.fetchMessages(
            anchor = "newest",
            numBefore = Const.MAX_MESSAGE_COUNT,
            numAfter = 0,
            topic = "",
            streamId = null,
            query = ""
        ).retryWhen { throwable -> throwable.delay(Const.DELAY, TimeUnit.SECONDS) }
         .subscribeOn(Schedulers.io())
         .doOnError{ Log.e("error", it.message.orEmpty()) }
    }

    private fun refreshLocalDataSource(streams: List<Stream>, isSubscribed: Boolean) {
        streamDao.deleteStreams(isSubscribed)
        streams.map {
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
