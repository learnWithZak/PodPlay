package com.zak.podplay.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zak.podplay.model.Episode
import com.zak.podplay.model.Podcast
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Podcast::class, Episode::class], version = 1)
abstract class PodPlayDatabase: RoomDatabase() {

    abstract fun podcastDao(): PodcastDao

    companion object {

        @Volatile
        private var INSTANCE: PodPlayDatabase? = null

        fun getInstance(context: Context, coroutineScope: CoroutineScope): PodPlayDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PodPlayDatabase::class.java,
                "PodPlayer")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}