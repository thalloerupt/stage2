package com.thallo.stage.download

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thallo.stage.databinding.ItemDownloadBinding
import rxhttp.wrapper.utils.query


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
                if(bean.state==2){
                getInsertUri().query(mContext, bean.filename, relativePath)
                    ?.let { it1 -> open(mContext, it1) }
                }

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

    fun open(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.clipData = ClipData.newRawUri("", uri);
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION
        intent.setDataAndType(uri,"*/*");
        context.startActivity(intent)

    }
}