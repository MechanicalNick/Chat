package com.tinkoff.homework.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.tinkoff.homework.data.db.AppDatabase
import com.tinkoff.homework.data.db.dao.MessageDao
import com.tinkoff.homework.data.db.dao.StreamDao
import com.tinkoff.homework.utils.Const.DB_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return databaseBuilder(
            context,
            AppDatabase::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideStreamDao(db: AppDatabase): StreamDao {
        return db.streamDao()
    }

    @Singleton
    @Provides
    fun provideMessageDao(db: AppDatabase): MessageDao {
        return db.messageDao()
    }
}