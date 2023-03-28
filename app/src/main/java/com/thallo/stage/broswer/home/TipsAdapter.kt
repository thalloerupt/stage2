package com.thallo.stage.broswer.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thallo.stage.broswer.history.HistoryListCallback
import com.thallo.stage.database.history.History
import com.thallo.stage.databinding.ItemSearchingTipsBinding

class TipsAdapter : ListAdapter<History, TipsAdapter.ItemTestViewHolder>(HistoryListCallback) {
    lateinit var select: Select

    inner class ItemTestViewHolder(private val binding: ItemSearchingTipsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bean: History, mContext: Context){
            binding.textView26.text=bean.title
            binding.textView27.text=bean.url
            binding.tipsItem.setOnClickListener { select.onSelect(bean.url) }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTestViewHolder {
        return ItemTestViewHolder(ItemSearchingTipsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemTestViewHolder, position: Int) {
        //通过ListAdapter内部实现的getItem方法找到对应的Bean
        holder.bind(getItem(holder.adapterPosition),holder.itemView.context)

    }
    interface Select{
        fun onSelect(url: String)
    }


}