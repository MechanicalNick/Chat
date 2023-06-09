package com.tinkoff.homework.data

import com.tinkoff.homework.data.dto.*
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface ZulipChatApi {
    @GET("streams")
    fun getAllStreams(): Single<StreamResponse>

    @GET("users/me/subscriptions")
    fun getSubscriptions(): Single<SubscriptionsResponse>

    @POST("users/me/subscriptions")
    fun subscribeOnStream(
        @Query("subscriptions") subscriptions: String
    ):Single<SubscribeOnStreamResponse>

    @GET("users/me/{stream_id}/topics")
    fun getAllTopics(@Path("stream_id") streamId: Long): Single<TopicResponse>

    @GET("users")
    fun getAllPeoples(): Single<PeopleResponse>

    @GET("realm/presence")
    fun getAllPresence(): Single<PresencesResponse>

    @GET("users/{userId}/presence")
    fun getPresence(@Path("userId") userId: Long): Single<PresenceResponse>

    @GET("users/{userId}")
    fun getUserInfo(@Path("userId") userId: Long): Single<UserResponse>

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String = "newest",
        @Query("num_before") numBefore: Long,
        @Query("num_after") numAfter: Long,
        @Query("narrow") narrow: String?,
        @Query("apply_markdown") markdown: Boolean = false,
    ): Single<MessagesResponse>

    @POST("messages/{messageId}/reactions")
    fun addReaction(
        @Path("messageId") messageId: Long,
        @Query("emoji_name") emojiName: String,
    ): Single<MessageResponse>

    @DELETE("messages/{messageId}/reactions")
    fun removeReaction(
        @Path("messageId") messageId: Long,
        @Query("emoji_name") emojiName: String,
    ): Single<MessageResponse>

    @POST("messages")
    fun sendMessage(
        @Query("to") streamId: Long,
        @Query("topic") topic: String,
        @Query("content") message: String,
        @Query("type") type: String = "stream"
    ): Single<MessageResponse>

    @DELETE("messages/{messageId}")
    fun removeMessage(
        @Path("messageId") messageId: Long
    ): Single<MessageResponse>

    @PATCH("messages/{messageId}")
    fun editMessage(
        @Path("messageId") messageId: Long,
        @Query("content") content: String
    ): Single<MessageResponse>

    @PATCH("messages/{messageId}")
    fun changeTopic(
        @Path("messageId") messageId: Long,
        @Query("topic") topic: String
    ): Single<MessageResponse>

    @Multipart
    @POST("user_uploads")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ): Single<ImageResponse>
}