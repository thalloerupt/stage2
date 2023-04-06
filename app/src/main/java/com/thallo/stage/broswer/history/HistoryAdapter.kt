package com.thallo.stage.broswer.history

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thallo.stage.R
import com.thallo.stage.database.history.History
import com.thallo.stage.databinding.ItemBookmarkBinding

class HistoryAdapter : ListAdapter<History, HistoryAdapter.ItemTestViewHolder>(HistoryListCallback) {
    lateinit var select: Select
    lateinit var popupSelect: PopupSelect
    companion object {
        var DELETE = 0
        var ADD_TO_HOMEPAGE = 1
    }

    inner class ItemTestViewHolder(private val binding: ItemBookmarkBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bean: History, mContext: Context){
            binding.textView9.text=bean.title
            binding.textView10.text=bean.url
            binding.bookmarkItem.setOnClickListener { bean.url?.let { it1 -> select.onSelect(it1) } }
            binding.materialButton18.setOnClickListener { v -> showMenu(v,bean,mContext) }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTestViewHolder {
        return ItemTestViewHolder(ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemTestViewHolder, position: Int) {
        //通过ListAdapter内部实现的getItem方法找到对应的Bean
        holder.bind(getItem(holder.adapterPosition),holder.itemView.context)

    }
    interface Select{
        fun onSelect(url: String)
    }
    interface PopupSelect{
        fun onPopupSelect(bean: History,item:Int)
    }

    private fun showMenu(v: View, bean: History,context: Context) {
        val popup = PopupMenu(context!!, v)
        popup.menuInflater.inflate(R.menu.bookmark_item_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            // Respond to menu item click.
            when(menuItem.itemId){
                R.id.menu_bookmark_item_delete -> {
                    popupSelect.onPopupSelect(bean, DELETE)

                }
                R.id.menu_bookmark_item_add_home ->{
                    popupSelect.onPopupSelect(bean, ADD_TO_HOMEPAGE)

                }
            }
            false
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }
}