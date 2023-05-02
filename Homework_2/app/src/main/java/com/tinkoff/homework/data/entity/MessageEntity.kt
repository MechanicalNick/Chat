package com.tinkoff.homework.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "streamId") val streamId: Long,
    @ColumnInfo(name = "topicName") val topicName: String,
    @ColumnInfo(name = "senderId") val senderId: Long,
    @ColumnInfo(name = "senderFullName") val senderFullName: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "avatarUrl") val avatarUrl: String
)