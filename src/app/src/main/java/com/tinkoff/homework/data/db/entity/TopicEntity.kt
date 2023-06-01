package com.tinkoff.homework.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "topic",
    primaryKeys = ["streamId", "name"],
    foreignKeys = [ForeignKey(
        entity = StreamEntity::class,
        parentColumns = arrayOf("streamId"),
        childColumns = arrayOf("streamId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class TopicEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "messageCount") val messageCount: Long,
    @ColumnInfo(name = "streamId") val streamId: Long
)