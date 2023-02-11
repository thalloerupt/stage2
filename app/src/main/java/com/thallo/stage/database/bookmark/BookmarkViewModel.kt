package com.thallo.stage.database.bookmark

import androidx.lifecycle.ViewModel

class BookmarkViewModel (bookmarkDao: BookmarkDao):ViewModel(){
    var bookmarkData=bookmarkDao.getBookmarkData()
}