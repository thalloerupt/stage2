package com.thallo.stage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.thallo.stage.database.bookmark.Bookmark
import com.thallo.stage.database.bookmark.BookmarkDao
import com.thallo.stage.database.history.History
import com.thallo.stage.database.history.HistoryDao
import com.thallo.stage.database.shortcut.Shortcut
import com.thallo.stage.database.shortcut.ShortcutDao

@Database(entities = [Bookmark::class, History::class,Shortcut::class], version = 1, exportSchema = false)
abstract class StageData : RoomDatabase() {
    abstract val historyDao: HistoryDao?
    abstract val bookmarkDao: BookmarkDao?
    abstract val shortcutDao: ShortcutDao?

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