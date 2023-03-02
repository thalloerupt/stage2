package com.thallo.stage.componets.bookmark.shortcut

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.thallo.stage.database.shortcut.Shortcut

object ShortcutListCallback : DiffUtil.ItemCallback<Shortcut>() {
    override fun areItemsTheSame(oldItem: Shortcut, newItem: Shortcut): Boolean {
        return oldItem ==newItem

    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Shortcut, newItem: Shortcut): Boolean {
        return oldItem ==newItem
    }

}