package com.thallo.stage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thallo.stage.database.bookmark.Bookmark
import com.thallo.stage.database.bookmark.BookmarkDao
import com.thallo.stage.database.history.History
import com.thallo.stage.database.history.HistoryDao

@Database(entities = [Bookmark::class, History::class], version = 1,exportSchema = false)
abstract class StageData : RoomDatabase() {
    abstract fun getBookmarkDao(): BookmarkDao
    abstract fun getHistoryDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: StageData? = null
        @JvmStatic
        fun getInstance(context: Context): StageData {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            //锁
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, StageData::class.java, "userDb").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}