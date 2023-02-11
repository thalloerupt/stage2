package com.thallo.stage.componets

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.thallo.stage.session.SessionDelegate
import mozilla.components.concept.storage.BookmarkNode

object BookmarkListCallback : DiffUtil.ItemCallback<BookmarkNode>() {
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