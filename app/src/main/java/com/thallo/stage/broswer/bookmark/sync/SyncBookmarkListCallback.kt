package com.thallo.stage.broswer.bookmark.sync

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import mozilla.components.concept.storage.BookmarkNode

object SyncBookmarkListCallback : DiffUtil.ItemCallback<BookmarkNode>() {
    override fun areItemsTheSame(oldItem: BookmarkNode, newItem: BookmarkNode): Boolean {
        return oldItem.title == newItem.title
                && oldItem.url == newItem.url

    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: BookmarkNode, newItem: BookmarkNode): Boolean {
        return oldItem.title == newItem.title
                && oldItem.url == newItem.url
    }

}