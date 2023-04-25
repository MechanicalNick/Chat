package com.tinkoff.homework.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.dto.ImageResponse
import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.data.dto.NarrowDto
import com.tinkoff.homework.db.dao.MessageDao
import com.tinkoff.homework.repository.interfaces.MessageRepository
import com.tinkoff.homework.utils.FileUtils
import com.tinkoff.homework.utils.ZulipChatApi
import com.tinkoff.homework.utils.mapper.toDomainMessage
import com.tinkoff.homework.utils.mapper.toMessageDomain
import com.tinkoff.homework.utils.mapper.toMessageEntity
import com.tinkoff.homework.utils.mapper.toReactionEntity
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(): MessageRepository {
    @Inject
    lateinit var api: ZulipChatApi

    @Inject
    lateinit var moshi: Moshi

    @Inject
    lateinit var messageDao: MessageDao

    @Inject
    lateinit var context: Context

    override fun fetchMessages(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long?,
        query: String
    ): Single<List<MessageModel>> {
        return streamId?.let { Single.zip(loadLocalResults(it, topic),
            loadResultsFromServer(anchor, numBefore, numAfter, topic, streamId, query)){
            local, server ->
                val list = mutableListOf<MessageModel>()
                val repeatedRequest = server.count() == 1 && server.first().id == local.firstOrNull()?.id
                if (!repeatedRequest)
                    list.addAll(server)
                list.addAll(local)
                list
            }
        } ?: run {
            loadResultsFromServer(anchor, numBefore, numAfter, topic, streamId, query)
        }
    }

    override fun fetchCashedMessages(streamId: Long, topic: String): Single<List<MessageModel>> {
        return loadLocalResults(streamId, topic)
    }

    override fun addReaction(messageId: Long, emojiName: String): Single<MessageResponse> {
        return api.addReaction(messageId, emojiName)
    }

    override fun removeReaction(messageId: Long, emojiName: String): Single<MessageResponse> {
        return api.removeReaction(messageId, emojiName)
    }

    override fun sendMessage(
        streamId: Long,
        topic: String,
        message: String
    ): Single<MessageResponse> {
        return api.sendMessage(streamId, topic, message)
    }

    override fun sendImage(uri: Uri): Single<ImageResponse> {
        val bytes = FileUtils.getBytes(uri, context)
        val requestFile = bytes.toRequestBody("image/*".toMediaTypeOrNull())
        val fileName = FileUtils.getFileNameFromURL(uri)
        val part = MultipartBody.Part.createFormData("imageName", fileName, requestFile)
        return api.uploadFile(part)
    }

    private fun loadResultsFromServer(
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
            narrow(topic, streamId, query)
        ).map { message -> message.messages
            .filter { m -> m.streamId != null && m.subject != null }
            .map { m -> toMessageDomain(m) }
        }

        if(streamId != null) {
            result.subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        refreshLocalDataSource(it, streamId, topic)
                    }, {
                        Log.e("error", it.message ?: it.stackTraceToString())
                    }
                )
        }

        return result
    }

    private fun loadLocalResults(streamId: Long, topicName: String): Single<List<MessageModel>> {
        return messageDao.getAll(streamId, topicName)
            .map { list -> list.map { result -> toDomainMessage(result) } }
    }

    private fun refreshLocalDataSource(
        messages: List<MessageModel>,
        streamId: Long,
        topic: String
    ) {
        messages.map { message ->
            messageDao.insertMessage(
                toMessageEntity(message, streamId, topic),
                message.reactions.map { reaction -> toReactionEntity(reaction, message.id) }
            )
        }
    }

    private fun narrow(
        topic: String,
        streamId: Long?,
        query: String
    ): String? {
        val list = mutableListOf<NarrowDto>()

        if (topic.isNotBlank())
            list.add(NarrowDto(operator = "topic", operand = topic))

        if(streamId != null)
            list.add(NarrowDto(operator = "stream", operand = streamId))

        if (query.isNotBlank())
            list.add(NarrowDto(operator = "search", operand = query))

        val type = Types.newParameterizedType(
            List::class.java,
            NarrowDto::class.java,
        )
        val moshi = Moshi.Builder().build()
        var adapter = moshi.adapter<List<NarrowDto>>(type)

        return if(list.isEmpty()) null else adapter.toJson(list)
    }
}