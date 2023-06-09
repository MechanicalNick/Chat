package com.tinkoff.homework.data.db.dao

import androidx.room.*
import com.tinkoff.homework.data.db.entity.MessageEntity
import com.tinkoff.homework.data.db.entity.ReactionEntity
import com.tinkoff.homework.data.db.entity.results.MessageResult
import io.reactivex.Single

@Dao
interface MessageDao {
    @Transaction
    @Query("SELECT * FROM message WHERE streamId =:streamId AND topicName =:topicName")
    fun getAllByTopic(streamId: Long, topicName: String): Single<List<MessageResult>>

    @Transaction
    @Query("SELECT * FROM message WHERE streamId =:streamId")
    fun getAll(streamId: Long): Single<List<MessageResult>>

    @Transaction
    fun insertMessage(messageEntity: MessageEntity, reactions: List<ReactionEntity>) {
        insert(messageEntity)
        insertReactions(reactions)
    }

    @Query("SELECT * FROM message WHERE id =:messageID LIMIT 1")
    fun get(messageID: Long): MessageEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messageEntity: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReactions(reactions: List<ReactionEntity>)

    @Query("DELETE FROM message WHERE streamId =:streamId")
    fun deleteMessages(streamId: Long)

    @Query("DELETE FROM message WHERE streamId =:streamId AND topicName =:topicName")
    fun deleteMessagesByTopic(streamId: Long, topicName: String)

    @Query("DELETE FROM message WHERE id =:messageId")
    fun deleteMessage(messageId: Long)
}