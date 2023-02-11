package com.thallo.stage.database.bookmark

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookmarkDao {
    //添加用户
    @Insert
    fun addBookmark(vararg bookmark: Bookmark)
    //查找用户
    //返回Bookmark表中所有的数据,使用LiveData
    @Query("select * from Bookmark")
    fun getBookmarkData(): LiveData<List<Bookmark>>
}