package com.thallo.stage.database.bookmark

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDao {
    @Insert
    fun insertBookmark(vararg bookmarks: Bookmark?)

    @Update
    fun updateBookmark(vararg bookmarks: Bookmark?)

    @Delete
    fun deleteBookmark(vararg bookmarks: Bookmark?)

    @Query("Delete FROM BOOKMARK")
    fun deleteAllBookmark()

    @get:Query("SELECT * FROM Bookmark ORDER BY ID DESC")
    val allBookmarksLive: LiveData<List<Bookmark?>?>?

    @Query("SELECT * FROM Bookmark WHERE url_info LIKE:pattern ORDER BY ID DESC")
    fun findBookmarksWithPattern(pattern: String?): LiveData<List<Bookmark?>?>?

    @Query("SELECT * FROM Bookmark WHERE title_info LIKE:pattern ORDER BY ID DESC")
    fun findBookmarksWithTitle(pattern: String?): LiveData<List<Bookmark?>?>?

    @Query("SELECT * FROM Bookmark WHERE show_info LIKE:pattern ORDER BY ID DESC")
    fun findBookmarksWithShow(pattern: Boolean?): LiveData<List<Bookmark?>?>?
}