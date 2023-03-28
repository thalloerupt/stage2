package com.thallo.stage.broswer.history

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.thallo.stage.database.bookmark.Bookmark
import com.thallo.stage.database.history.History
import mozilla.components.concept.storage.BookmarkNode

object HistoryListCallback : DiffUtil.ItemCallback<History>() {
    override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
        return oldItem ==newItem

    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
        return oldItem ==newItem
    }

}