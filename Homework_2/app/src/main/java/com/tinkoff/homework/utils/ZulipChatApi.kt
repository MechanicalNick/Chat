package com.tinkoff.homework.utils

import com.tinkoff.homework.data.dto.*
import io.reactivex.Single
import retrofit2.http.*

interface ZulipChatApi {
    @GET("streams")
    fun getAllStreams(): Single<StreamResponse>

    @GET("users/me/subscriptions")
    fun getSubscriptions(): Single<SubscriptionsResponse>

    @GET("users/me/{stream_id}/topics")
    fun getAllTopics(@Path("stream_id") streamId: Long): Single<TopicResponse>

    @GET("users")
    fun getAllPeoples(): Single<PeopleResponse>

    @GET("realm/presence")
    fun getAllPresence(): Single<PresencesResponse>

    @GET("users/me")
    fun getMyInfo(): Single<PeopleDto>

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String = "newest",
        @Query("num_before") numBefore: Long,
        @Query("num_after") numAfter: Long,
        @Query("narrow") narrow: String,
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
}