package com.thallo.stage.componets

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thallo.stage.databinding.ItemAddonsManagerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mozilla.geckoview.*

class AddonsAdapter : ListAdapter<WebExtension, AddonsAdapter.ItemTestViewHolder>(MenuAddonsListCallback) {
    lateinit var select: Select

    inner class ItemTestViewHolder(private val binding: ItemAddonsManagerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bean: WebExtension, mContext: Context){
            mContext as LifecycleOwner
            var webExtensionController=GeckoRuntime.getDefault(mContext).webExtensionController
            mContext.lifecycleScope.launch {
                binding.textView8.text = bean.metaData.name
            }
            bean.metaData.icon.getBitmap(72).accept { binding.imageView3.setImageBitmap(it) }

            binding.materialCardView4.setOnClickListener { select.onSelect(bean) }
            binding.switch1.isChecked=bean.metaData.enabled
            binding.switch1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
                if (b) webExtensionController.enable(
                    bean,
                    WebExtensionController.EnableSource.USER
                ) else webExtensionController.disable(
                    bean,
                    WebExtensionController.EnableSource.USER
                )
            })

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTestViewHolder {
        return ItemTestViewHolder(ItemAddonsManagerBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemTestViewHolder, position: Int) {
        //通过ListAdapter内部实现的getItem方法找到对应的Bean
        holder.bind(getItem(holder.adapterPosition),holder.itemView.context)

    }
    interface Select{
        fun onSelect(bean: WebExtension)
    }

}