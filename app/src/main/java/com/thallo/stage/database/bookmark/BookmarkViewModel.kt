package com.thallo.stage.database.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    var bookmarkRepository: BookmarkRepository

    init {
        bookmarkRepository = BookmarkRepository(application)
    }

    val allBookmarksLive: LiveData<List<Bookmark?>?>?
        get() = bookmarkRepository.allBookmarkLive

    fun findBookmarksWithPattern(pattern: String): LiveData<List<Bookmark?>?>? {
        return bookmarkRepository.findBookmarksWithPattern(pattern)
    }

    fun findBookmarksWithTitle(pattern: String?): LiveData<List<Bookmark?>?>? {
        return bookmarkRepository.findBookmarksWithTitle(pattern)
    }

    fun findBookmarksWithShow(pattern: Boolean?): LiveData<List<Bookmark?>?>? {
        return bookmarkRepository.findBookmarksWithShow(pattern)
    }

    fun insertBookmarks(vararg bookmarks: Bookmark?) {
        bookmarkRepository.insertBookmark(*bookmarks)
    }

    fun updateBookmarks(vararg bookmarks: Bookmark?) {
        bookmarkRepository.updateBookmark(*bookmarks)
    }

    fun deleteBookmarks(vararg bookmarks: Bookmark?) {
        bookmarkRepository.deleteBookmark(*bookmarks)
    }

    fun deleteAllBookmarks() {
        bookmarkRepository.deleteAllbookmarks()
    }
}