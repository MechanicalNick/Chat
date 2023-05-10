package com.tinkoff.homework.data.repository

import android.util.Log
import com.squareup.moshi.Moshi
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.Stream
import com.tinkoff.homework.domain.data.Topic
import com.tinkoff.homework.data.dto.TopicDto
import com.tinkoff.homework.data.dto.TopicResponse
import com.tinkoff.homework.data.db.dao.StreamDao
import com.tinkoff.homework.domain.repository.MessageRepository
import com.tinkoff.homework.domain.repository.StreamRepository
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.data.ZulipChatApi
import com.tinkoff.homework.data.dto.SubscribeOnStreamResponse
import com.tinkoff.homework.data.mapper.toDomain
import com.tinkoff.homework.data.mapper.toEntities
import com.tinkoff.homework.data.mapper.toEntity
import com.tinkoff.homework.data.mapper.toSubscription
import com.tinkoff.homework.utils.zipSingles
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class StreamRepositoryImpl @Inject constructor(
    private val api: ZulipChatApi,
    private val messageRepository: MessageRepository,
    private val streamDao: StreamDao,
    private val moshi: Moshi,
) : StreamRepository {

    override fun subscribeOnStream(streamName: String): Single<SubscribeOnStreamResponse> {
        return api
            .subscribeOnStream(toSubscription(moshi, streamName))
            .subscribeOn(Schedulers.io())
    }

    override fun getAll(): Single<List<Stream>> {
        return api.getAllStreams()
            .map { response -> response.streams }
            .map { list ->
                list.map { dto ->
                    Stream(
                        id = dto.streamId,
                        name = dto.name,
                        topics = mutableListOf(),
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
                        id = dto.streamId,
                        name = dto.name,
                        topics = mutableListOf(),
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
                if (query.isBlank()) {
                    true
                } else {
                    stream.name.contains(query, ignoreCase = true)
                }
            }
            .toList()
    }

    override fun fetchCashedResults(isSubscribed: Boolean): Single<List<Stream>> {
        return loadLocalResults(isSubscribed)
    }

    private fun loadResultsFromServer(isSubscribed: Boolean): Single<List<Stream>> {
        val collection = if (isSubscribed) getSubscriptions() else getAll()

        val result = Single.zip(collection, fetchMessages()) { list, messages ->
            Pair(list, messages)
        }   .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap { pair ->
                pair.first.map { stream ->
                    Single.zip(
                        Single.just(stream),
                        api.getAllTopics(stream.id)
                    ) { curStream, topics ->
                        val newTopics = createTopics(topics, pair, curStream)
                        curStream.topics.addAll(newTopics)
                        curStream
                    }
                }.zipSingles()
            }.doOnSuccess {
                refreshLocalDataSource(it, isSubscribed)
            }

        return result
    }

    private fun createTopics(
        topics: TopicResponse,
        pair: Pair<List<Stream>, List<MessageModel>>,
        curStream: Stream
    ): List<Topic> {
        return topics.topics.map { topicDto ->
            Topic(
                topicDto.name,
                getMessageCount(pair, topicDto, curStream),
                curStream.name,
                curStream.id
            )
        }
    }

    private fun getMessageCount(
        pair: Pair<List<Stream>, List<MessageModel>>,
        topicDto: TopicDto,
        curStream: Stream
    ) = pair.second.count { messageModel ->
        messageModel.subject == topicDto.name &&
                messageModel.streamId == curStream.id
    }.toLong()

    private fun loadLocalResults(isSubscribed: Boolean): Single<List<Stream>> {
        val collection =
            if (isSubscribed) streamDao.getSubscribed(onlySubscribed = true) else streamDao.getAll()
        return collection
            .map { list -> list.map { streamResult -> streamResult.toDomain() } }
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
                    stream = it.toEntity(isSubscribed = true),
                    topics = it.toEntities()
                )
            } else {
                streamDao.insertStreamWithIgnoreStrategy(
                    stream = it.toEntity(isSubscribed = false),
                    topics = it.toEntities()
                )
            }
        }
    }
}
