package com.tinkoff.homework.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "streamId") val streamId: Long,
    @ColumnInfo(name = "topicName") val topicName: String,
    @ColumnInfo(name = "senderId") val senderId: Long,
    @ColumnInfo(name = "senderFullName") val senderFullName: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "dateTime") val dateTime: LocalDateTime,
    @ColumnInfo(name = "avatarUrl") val avatarUrl: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String?,
)