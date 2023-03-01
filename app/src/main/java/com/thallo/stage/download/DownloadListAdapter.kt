package com.thallo.stage.download

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thallo.stage.databinding.ItemDownloadBinding
import java.io.File


class DownloadListAdapter : ListAdapter<DownloadTask, DownloadListAdapter.ItemTestViewHolder>(DownListCallback) {
    private val relativePath: String = Environment.DIRECTORY_DOWNLOADS
    @RequiresApi(Build.VERSION_CODES.Q)
    fun getInsertUri() = MediaStore.Downloads.EXTERNAL_CONTENT_URI

    inner class ItemTestViewHolder(private val binding: ItemDownloadBinding): RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.Q)
        fun bind(bean:DownloadTask, mContext: Context){
            binding.task=getItem(adapterPosition)
            binding.downloadButton2.setOnClickListener{
                if(bean.state==0)
                    bean.open()
                else
                    bean.pause()
            }
            binding.materialCardView5.setOnClickListener {



                val imagePath = File(Environment.getExternalStorageDirectory(), "")
                val newFile = File(imagePath, bean.filename)

            }




        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTestViewHolder {
        return ItemTestViewHolder(ItemDownloadBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: ItemTestViewHolder, position: Int) {
        //通过ListAdapter内部实现的getItem方法找到对应的Bean
        holder.bind(getItem(holder.adapterPosition),holder.itemView.context)

    }


}