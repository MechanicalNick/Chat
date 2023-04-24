package com.tinkoff.homework.db.dao

import androidx.room.*
import com.tinkoff.homework.data.entity.MessageEntity
import com.tinkoff.homework.data.entity.ReactionEntity
import com.tinkoff.homework.data.results.MessageResult
import io.reactivex.Single

@Dao
interface MessageDao {
    @Transaction
    @Query("SELECT * FROM message WHERE streamId =:streamId AND topicName =:topicName")
    fun getAll(streamId: Long, topicName: String): Single<List<MessageResult>>

    @Transaction
    fun insertMessage(messageEntity: MessageEntity, reactions: List<ReactionEntity>) {
        insert(messageEntity)
        insertReactions(reactions)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messageEntity: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReactions(reactions: List<ReactionEntity>)

    @Query("DELETE FROM message WHERE streamId =:streamId AND topicName =:topicName")
    fun deleteMessages(streamId: Long, topicName: String)
}