package com.tinkoff.homework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tinkoff.homework.data.entity.MessageEntity
import com.tinkoff.homework.data.entity.ReactionEntity
import com.tinkoff.homework.data.entity.StreamEntity
import com.tinkoff.homework.data.entity.TopicEntity
import com.tinkoff.homework.db.dao.MessageDao
import com.tinkoff.homework.db.dao.StreamDao


@Database(
    entities = [StreamEntity::class, TopicEntity::class, MessageEntity::class,
        ReactionEntity::class], version = 12
)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun streamDao(): StreamDao
    abstract fun messageDao(): MessageDao
}