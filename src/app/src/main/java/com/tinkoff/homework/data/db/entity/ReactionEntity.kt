package com.tinkoff.homework.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reaction",
    foreignKeys = [ForeignKey(
        entity = MessageEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("messageId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ReactionEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "messageId") val messageId: Long,
    @ColumnInfo(name = "userId") val userId: Long,
    @ColumnInfo(name = "emojiCode") val emojiCode: String,
    @ColumnInfo(name = "emojiName") val emojiName: String
)