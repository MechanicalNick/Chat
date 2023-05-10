package com.tinkoff.homework.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stream")
data class StreamEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "streamId") val streamId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "isSubscribed") val isSubscribed: Boolean,
)