package com.thallo.stage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.thallo.stage.database.bookmark.Bookmark
import com.thallo.stage.database.bookmark.BookmarkDao
import com.thallo.stage.database.history.History
import com.thallo.stage.database.history.HistoryDao

@Database(entities = [Bookmark::class, History::class], version = 1, exportSchema = false)
abstract class StageData : RoomDatabase() {
    abstract val historyDao: HistoryDao?
    abstract val bookmarkDao: BookmarkDao?

    companion object {
        private var INSTANCE: StageData? = null
        @JvmStatic
        @Synchronized
        fun getDatabase(context: Context): StageData? {
            if (INSTANCE == null) {
                INSTANCE = databaseBuilder(
                    context.applicationContext,
                    StageData::class.java,
                    "UserDatabase"
                )
                    .build()
            }
            return INSTANCE
        }
    }
}