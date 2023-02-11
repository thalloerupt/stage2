package com.thallo.stage.download

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thallo.stage.R
import com.thallo.stage.databinding.ItemDownloadBinding
import com.thallo.stage.databinding.ItemTablistPhoneBinding
import com.thallo.stage.session.DelegateLivedata
import com.thallo.stage.session.SessionDelegate
import com.thallo.stage.session.SessionViewModel
import com.thallo.stage.tab.TabListCallback

class DownloadListAdapter : ListAdapter<DownloadTask, DownloadListAdapter.ItemTestViewHolder>(DownListCallback) {

    inner class ItemTestViewHolder(private val binding: ItemDownloadBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bean:DownloadTask,mContext: Context){
            binding.task=getItem(adapterPosition)
            if(bean.state==0)
                binding.downloadButton2.icon=mContext.getDrawable(R.drawable.play_circle)
            else
                binding.downloadButton2.icon=mContext.getDrawable(R.drawable.pause_circle)


            binding.downloadButton2.setOnClickListener{
                if(bean.state==0){
                    bean.open()
                    binding.downloadButton2.icon=mContext.getDrawable(R.drawable.pause_circle)
                }
                else{
                    bean.pause()
                    binding.downloadButton2.icon=mContext.getDrawable(R.drawable.play_circle)

                }
            }




        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTestViewHolder {
        return ItemTestViewHolder(ItemDownloadBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemTestViewHolder, position: Int) {
        //通过ListAdapter内部实现的getItem方法找到对应的Bean
        holder.bind(getItem(holder.adapterPosition),holder.itemView.context)

    }


}