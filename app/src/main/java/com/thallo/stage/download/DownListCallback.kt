package com.thallo.stage.download

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thallo.stage.session.SessionDelegate

object DownListCallback : DiffUtil.ItemCallback<DownloadTask>() {
    override fun areItemsTheSame(oldItem: DownloadTask, newItem: DownloadTask): Boolean {
        return oldItem.currentSize == newItem.currentSize
                && oldItem.title == newItem.title
                && oldItem.progress == newItem.progress
                && oldItem.totalSize == newItem.totalSize
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DownloadTask, newItem: DownloadTask): Boolean {
        return oldItem.currentSize == newItem.currentSize
                && oldItem.title == newItem.title
                && oldItem.progress == newItem.progress
                && oldItem.totalSize == newItem.totalSize
    }

}