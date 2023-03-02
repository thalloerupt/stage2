package com.thallo.stage.componets.bookmark.shortcut

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thallo.stage.R
import com.thallo.stage.database.bookmark.Bookmark
import com.thallo.stage.database.shortcut.Shortcut
import com.thallo.stage.databinding.ItemBookmarkBinding
import com.thallo.stage.databinding.ItemShortcutBinding
import mozilla.components.concept.storage.BookmarkNode
import java.net.URI

class ShortcutAdapter : ListAdapter<Shortcut, ShortcutAdapter.ItemTestViewHolder>(
ShortcutListCallback
) {
    lateinit var select: Select

    inner class ItemTestViewHolder(private val binding: ItemShortcutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bean: Shortcut, mContext: Context){

            binding.textView21.text=bean.title
            val uri = URI.create(bean.url)
            val faviconUrl = uri.scheme + "://" + uri.host + "/favicon.ico"
            Glide.with(mContext)
                .load(faviconUrl)
                .placeholder(R.drawable.logo72)
                .circleCrop()
                .into(binding.imageView10)
            binding.materialCardView6.setOnClickListener { bean.url?.let { it1 -> select.onSelect(it1) } }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTestViewHolder {
        return ItemTestViewHolder(ItemShortcutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemTestViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition),holder.itemView.context)
    }

    interface Select{
        fun onSelect(url: String)
    }

}