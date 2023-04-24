package com.tinkoff.homework.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "topic",
    foreignKeys = [ForeignKey(
        entity = StreamEntity::class,
        parentColumns = arrayOf("streamId"),
        childColumns = arrayOf("streamId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class TopicEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "messageCount") val messageCount: Long,
    @ColumnInfo(name = "streamId") val streamId: Long
)