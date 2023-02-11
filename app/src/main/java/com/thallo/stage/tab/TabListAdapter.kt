package com.thallo.stage.tab

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thallo.stage.databinding.ItemTablistPhoneBinding
import com.thallo.stage.session.DelegateLivedata
import com.thallo.stage.session.SessionDelegate
import com.thallo.stage.session.SessionViewModel

class TabListAdapter : ListAdapter<SessionDelegate, TabListAdapter.ItemTestViewHolder>(TabListCallback) {
    lateinit var select:Select

    inner class ItemTestViewHolder(private val binding: ItemTablistPhoneBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bean:SessionDelegate,mContext: Context){
            binding.user=getItem(adapterPosition)
            binding.wholeTab?.setOnClickListener{
                DelegateLivedata.getInstance().Value(getItem(adapterPosition))
                select.onSelect()
            }
            binding.materialButton?.setOnClickListener { RemoveTabLiveData.getInstance().Value(adapterPosition)
            }
            binding.deleteButton?.setOnClickListener { RemoveTabLiveData.getInstance().Value(adapterPosition)
            }



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTestViewHolder {
        return ItemTestViewHolder(ItemTablistPhoneBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemTestViewHolder, position: Int) {
        //通过ListAdapter内部实现的getItem方法找到对应的Bean
        holder.bind(getItem(holder.adapterPosition),holder.itemView.context)
        holder.itemView.context

    }
    interface Select{
        fun onSelect()
    }

}