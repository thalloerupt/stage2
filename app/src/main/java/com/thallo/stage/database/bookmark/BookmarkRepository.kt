package com.thallo.stage.database.bookmark

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.thallo.stage.database.StageData.Companion.getDatabase

class BookmarkRepository internal constructor(context: Context) {
    var bookmarkDao: BookmarkDao?
    var allBookmarkLive: LiveData<List<Bookmark?>?>?

    init {
        val stageData = getDatabase(context.applicationContext)
        bookmarkDao = stageData!!.bookmarkDao
        allBookmarkLive = bookmarkDao?.allBookmarksLive
    }

    fun insertBookmark(vararg bookmarks: Bookmark?) {
        InsertAsyncTask(bookmarkDao).execute(*bookmarks)
    }

    fun updateBookmark(vararg bookmarks: Bookmark?) {
        UpdateAsyncTask(bookmarkDao).execute(*bookmarks)
    }

    fun deleteBookmark(vararg bookmarks: Bookmark?) {
        DeleteAsyncTask(bookmarkDao).execute(*bookmarks)
    }

    fun deleteAllbookmarks() {
        DeleteAllAsyncTask(bookmarkDao).execute()
    }

    fun findBookmarksWithPattern(pattern: String): LiveData<List<Bookmark?>?>? {
        return bookmarkDao!!.findBookmarksWithPattern("%$pattern%")
    }

    fun findBookmarksWithTitle(pattern: String?): LiveData<List<Bookmark?>?>? {
        return bookmarkDao!!.findBookmarksWithTitle(pattern)
    }

    fun findBookmarksWithShow(pattern: Boolean?): LiveData<List<Bookmark?>?>? {
        return bookmarkDao!!.findBookmarksWithShow(pattern)
    }

    internal class InsertAsyncTask(private val bookmarkDao: BookmarkDao?) :
        AsyncTask<Bookmark?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: Bookmark?): Void? {
            bookmarkDao!!.insertBookmark(*params)
            return null
        }
    }

    internal class UpdateAsyncTask(private val bookmarkDao: BookmarkDao?) :
        AsyncTask<Bookmark?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: Bookmark?): Void? {
            bookmarkDao!!.updateBookmark(*params)
            return null
        }
    }

    internal class DeleteAsyncTask(private val bookmarkDao: BookmarkDao?) :
        AsyncTask<Bookmark?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: Bookmark?): Void? {
            bookmarkDao!!.deleteBookmark(*params)
            return null
        }
    }

    internal class DeleteAllAsyncTask(private val bookmarkDao: BookmarkDao?) :
        AsyncTask<Void?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: Void?): Void? {
            bookmarkDao!!.deleteAllBookmark()
            return null
        }
    }
}