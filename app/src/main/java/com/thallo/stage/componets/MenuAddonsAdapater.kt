package com.thallo.stage.componets

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thallo.stage.databinding.ItemBookmarkBinding
import com.thallo.stage.databinding.ItemMenuAddonsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.WebExtension

class MenuAddonsAdapater : ListAdapter<WebExtension, MenuAddonsAdapater.ItemTestViewHolder>(MenuAddonsListCallback) {
    lateinit var select: Select

    inner class ItemTestViewHolder(private val binding: ItemMenuAddonsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bean: WebExtension, mContext: Context){
            mContext as LifecycleOwner
            val addonsPopup=AddonsPopup(mContext)
            bean.setActionDelegate(object :WebExtension.ActionDelegate{
                override fun onBrowserAction(
                    extension: WebExtension,
                    session: GeckoSession?,
                    action: WebExtension.Action
                ) {
                    mContext.lifecycleScope.launch {
                        binding.addonsIcon.setImageBitmap(withContext(Dispatchers.IO) {
                            action.icon?.getBitmap(
                                64
                            )?.poll()
                        })
                    }
                    binding.addonsIcon.setOnClickListener { action.click() }
                }
                override fun onTogglePopup(
                    extension: WebExtension,
                    action: WebExtension.Action
                ): GeckoResult<GeckoSession>? {
                    val session=GeckoSession()
                    addonsPopup.show(session,extension)
                    return GeckoResult.fromValue(session)
                }
                override fun onOpenPopup(
                    extension: WebExtension,
                    action: WebExtension.Action
                ): GeckoResult<GeckoSession>? {
                    val session=GeckoSession()
                    addonsPopup.show(session,extension)
                    return GeckoResult.fromValue(session)
                }
            })


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTestViewHolder {
        return ItemTestViewHolder(ItemMenuAddonsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemTestViewHolder, position: Int) {
        //通过ListAdapter内部实现的getItem方法找到对应的Bean
        holder.bind(getItem(holder.adapterPosition),holder.itemView.context)

    }
    interface Select{
        fun onSelect()
    }

}