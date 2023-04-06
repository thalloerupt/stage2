package com.thallo.stage.broswer.bookmark.sync

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thallo.stage.databinding.ItemSyncBookmarkFolderBinding
import mozilla.components.concept.storage.BookmarkNode

class SyncBookmarkFolderAdapter : ListAdapter<BookmarkNode, SyncBookmarkFolderAdapter.ItemTestViewHolder>(
    SyncBookmarkListCallback
) {
    lateinit var select: Select

    inner class ItemTestViewHolder(private val binding: ItemSyncBookmarkFolderBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bean: BookmarkNode, mContext: Context){
            binding.textView4.text=
                bean.title
                    ?.replace("toolbar","工具栏书签")
                    ?.replace("menu","菜单书签")
                    ?.replace("mobile","移动设备书签")
                    ?.replace("root","所有书签")
           // binding.textView10.text=bean.url
            binding.root.setOnClickListener { select.onSelect(bean)  }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTestViewHolder {
        return ItemTestViewHolder(ItemSyncBookmarkFolderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemTestViewHolder, position: Int) {
        //通过ListAdapter内部实现的getItem方法找到对应的Bean
        holder.bind(getItem(holder.adapterPosition),holder.itemView.context)

    }
    interface Select{
        fun onSelect(bean: BookmarkNode)
    }

}