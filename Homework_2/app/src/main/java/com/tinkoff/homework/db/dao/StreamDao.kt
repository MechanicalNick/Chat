package com.tinkoff.homework.db.dao

import androidx.room.*
import com.tinkoff.homework.data.entity.StreamEntity
import com.tinkoff.homework.data.entity.TopicEntity
import com.tinkoff.homework.data.results.StreamResult
import io.reactivex.Single

@Dao
interface StreamDao {
    @Transaction
    @Query("SELECT * FROM stream WHERE isSubscribed =:onlySubscribed")
    fun getSubscribed(onlySubscribed: Boolean): Single<List<StreamResult>>

    @Transaction
    @Query("SELECT * FROM stream")
    fun getAll(): Single<List<StreamResult>>

    @Query("SELECT EXISTS (SELECT 1 FROM stream)")
    fun containsData(): Boolean

    @Transaction
    fun insertStreamWithIgnoreStrategy(stream: StreamEntity, topics: List<TopicEntity>) {
        insertWithIgnoreStrategy(stream)
        insertTopicsWithIgnoreStrategy(topics)
    }

    @Transaction
    fun insertStreamWithReplaceStrategy(stream: StreamEntity, topics: List<TopicEntity>) {
        insertWithReplaceStrategy(stream)
        insertTopicsWithReplaceStrategy(topics)
    }

    @Query("DELETE FROM stream WHERE isSubscribed =:onlySubscribed")
    fun deleteStreams(onlySubscribed: Boolean)

    @Query("SELECT * FROM stream WHERE streamId =:streamId AND isSubscribed =:onlySubscribed")
    fun findByStreamIdAndIsSubscribed(streamId: Long, onlySubscribed: Boolean): StreamEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnoreStrategy(stream: StreamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWithReplaceStrategy(stream: StreamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopicsWithReplaceStrategy(topics: List<TopicEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTopicsWithIgnoreStrategy(topics: List<TopicEntity>)
}