package com.tinkoff.homework.data.repository

import android.content.Context
import android.net.Uri
import com.squareup.moshi.Moshi
import com.tinkoff.homework.data.ZulipChatApi
import com.tinkoff.homework.data.db.dao.MessageDao
import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.data.dto.ImageResponse
import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.data.mapper.toDomain
import com.tinkoff.homework.data.mapper.toEntity
import com.tinkoff.homework.data.mapper.toMyMessageEntity
import com.tinkoff.homework.data.mapper.toNarrow
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.repository.MessageRepository
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.utils.FileUtils
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val api: ZulipChatApi,
    private val moshi: Moshi,
    private val messageDao: MessageDao,
    private val context: Context,
    private val credentials: Credentials
) : MessageRepository {

    override fun fetchMessages(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long?,
        query: String
    ): Single<List<MessageModel>> {
        return streamId?.let {
            Single.zip(
                loadLocalResults(it, topic),
                loadResultsFromServer(anchor, numBefore, numAfter, topic, streamId, query)
            ){
            local, server ->
                val list = mutableListOf<MessageModel>()
                list.addAll(server)
                list.addAll(local)
                list.distinctBy{m -> m.id}
            }
        } ?: run {
            loadResultsFromServer(anchor, numBefore, numAfter, topic, streamId = null, query)
        }
    }

    override fun fetchCashedMessages(streamId: Long, topic: String): Single<List<MessageModel>> {
        return loadLocalResults(streamId, topic)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
    }

    override fun addReaction(messageId: Long, emojiName: String): Single<MessageResponse> {
        return api.addReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
    }

    override fun removeReaction(messageId: Long, emojiName: String): Single<MessageResponse> {
        return api.removeReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
    }

    override fun sendMessage(
        streamId: Long,
        topic: String,
        message: String
    ): Single<MessageResponse> {
        return api.sendMessage(streamId, topic, message)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnSuccess {
                messageDao.insert(it.toMyMessageEntity(credentials, streamId, topic))
            }
    }

    override fun sendImage(uri: Uri): Single<ImageResponse> {
        val bytes = FileUtils.getBytes(uri, context)
        val requestFile = bytes.toRequestBody("image/*".toMediaTypeOrNull())
        val fileName = FileUtils.getFileNameFromURL(uri)
        val part = MultipartBody.Part.createFormData(
            "imageName",
            fileName,
            requestFile
        )

        return api.uploadFile(part)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
    }

    override fun removeMessage(messageId: Long): Single<MessageResponse> {
        return api.removeMessage(messageId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnSuccess {
                messageDao.deleteMessage(messageId)
            }
    }

    override fun editMessage(messageId: Long, newText: String): Single<MessageResponse> {
        return api.editMessage(messageId, newText)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnSuccess {
                val oldEntity = messageDao.get(messageId)
                val newEntity = oldEntity.copy(text = newText)
                messageDao.insert(newEntity)
            }
    }

    override fun changeTopic(messageId: Long, newTopic: String): Single<MessageResponse> {
        return api.changeTopic(messageId, newTopic)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnSuccess {
                val oldEntity = messageDao.get(messageId)
                val newEntity = oldEntity.copy(text = newTopic)
                messageDao.insert(newEntity)
            }
    }

    override fun loadResultsFromServer(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long?,
        query: String
    ): Single<List<MessageModel>> {
        val result = api.getMessages(
            anchor,
            numBefore,
            numAfter,
            toNarrow(moshi, topic, streamId, query)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { message ->
                message.messages
                    .filter { m -> m.streamId != null && m.subject != null }
                    .map { m -> m.toDomain() }
            }
            .doOnSuccess { list ->
                streamId?.let {id ->
                    val needDelete = numBefore == Const.MAX_MESSAGE_COUNT_IN_DB
                    refreshLocalDataSource(list, id, topic, needDelete)
                }
            }
        return result
    }

    private fun loadLocalResults(streamId: Long, topicName: String): Single<List<MessageModel>> {
        val collection =
            if (topicName.isBlank()) messageDao.getAll(streamId) else messageDao.getAllByTopic(
                streamId,
                topicName
            )
        return collection
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { list -> list.map { result -> result.toDomain() } }
    }

    private fun refreshLocalDataSource(
        messages: List<MessageModel>,
        streamId: Long,
        topic: String,
        needDelete: Boolean
    ) {
        if(needDelete) {
            if(topic.isBlank())
                messageDao.deleteMessages(streamId)
            else
                messageDao.deleteMessagesByTopic(streamId, topic)
        }
        messages.map { message ->
            messageDao.insertMessage(
                message.toEntity(streamId),
                message.reactions.map { reaction -> reaction.toEntity(message.id) }
            )
        }
    }
}