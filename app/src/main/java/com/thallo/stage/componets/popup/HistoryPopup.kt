package com.thallo.stage.componets.popup

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.thallo.stage.MainActivity
import com.thallo.stage.R
import com.thallo.stage.componets.CollectionAdapter
import com.thallo.stage.componets.HomeLivedata
import com.thallo.stage.componets.bookmark.BookmarkFragment
import com.thallo.stage.componets.bookmark.sync.SyncBookmarkFragment
import com.thallo.stage.componets.history.HistoryFragment
import com.thallo.stage.databinding.PopupBookmarkBinding
import com.thallo.stage.databinding.PopupHistoryBinding

class HistoryPopup {
    val context: MainActivity
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var binding: PopupHistoryBinding

    private val fragments=listOf<Fragment>(HistoryFragment())
    constructor(
        context: MainActivity,
    ) {
        this.context = context
        bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog )
        binding = PopupHistoryBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(binding.root)
        binding.historyViewPager.adapter= CollectionAdapter(context,fragments)
        TabLayoutMediator(binding.historytabLayout,binding.historyViewPager){ tab,position->
            when (position) {
                0 -> tab.setIcon(R.drawable.hourglass_split)
            }
        }.attach()
        HomeLivedata.getInstance().observe(context){
            if (!it) bottomSheetDialog.dismiss()
        }

    }
    fun show(){
        bottomSheetDialog.show()
    }
}