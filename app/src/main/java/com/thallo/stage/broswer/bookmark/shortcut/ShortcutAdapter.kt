package com.thallo.stage.broswer.bookmark.shortcut

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
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
    lateinit var longClick: LongClick

    inner class ItemTestViewHolder(private val binding: ItemShortcutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bean: Shortcut, mContext: Context){

            binding.textView21.text=bean.title
            val uri = URI.create(bean.url)
            val faviconUrl = uri.scheme + "://" + uri.host + "/favicon.ico"
            Glide.with(mContext)
                .load(faviconUrl)
                .placeholder(R.drawable.globe)
                .into(binding.imageView10)
            binding.materialCardView8.setOnClickListener { bean.url?.let { it1 -> select.onSelect(it1) } }
            binding.materialCardView8.setOnLongClickListener {
                dialog(mContext,bean)
                false
            }


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
    interface LongClick{
        fun onLongClick(bean: Shortcut)
    }
    private fun dialog(context: Context,bean: Shortcut){
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.dialog_shortcut_title))
            .setNegativeButton(context.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(context.getString(R.string.confirm)) { dialog, which ->
                longClick.onLongClick(bean)
            }
            .show()
    }

}