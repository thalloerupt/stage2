package com.thallo.stage.componets.bookmark

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.thallo.stage.database.bookmark.Bookmark
import mozilla.components.concept.storage.BookmarkNode

object BookmarkListCallback : DiffUtil.ItemCallback<Bookmark>() {
    override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
        return oldItem ==newItem

    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
        return oldItem ==newItem
    }

}