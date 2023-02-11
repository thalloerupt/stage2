package com.thallo.stage.componets

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thallo.stage.HolderActivity
import com.thallo.stage.MainActivity
import com.thallo.stage.R
import com.thallo.stage.databinding.PopupMenuBinding
import com.thallo.stage.databinding.PopupTabBinding
import com.thallo.stage.webextension.WebextensionListLiveData
import org.mozilla.geckoview.GeckoRuntime

class MenuPopup{
    val context: MainActivity
    lateinit var bottomSheetDialog:BottomSheetDialog
    lateinit var binding: PopupMenuBinding
    constructor(
        context: MainActivity,
    ) {
        this.context = context
        bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog )
        binding = PopupMenuBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(binding.root)
        binding.downloadButton.setOnClickListener {
            var intent=Intent(context, HolderActivity::class.java)
            intent.putExtra("Page","DOWNLOAD")
            context.startActivity(intent)
        }
        binding.settingButton.setOnClickListener {
            var intent=Intent(context, HolderActivity::class.java)
            intent.putExtra("Page","SETTINGS")
            context.startActivity(intent)
        }
        binding.addonsButton.setOnClickListener {
            var intent=Intent(context, HolderActivity::class.java)
            intent.putExtra("Page","ADDONS")
            context.startActivity(intent)
        }
        binding.bookmarkButton.setOnClickListener {
            BookmarkPopup(context).show()
            bottomSheetDialog.dismiss()

        }
        var adapter= MenuAddonsAdapater()
        binding.menuAddonsRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.menuAddonsRecyclerView.adapter=adapter
        GeckoRuntime.getDefault(context).webExtensionController.list().accept {
            adapter.submitList(it)
        }

    }
    fun show(){
        bottomSheetDialog.show()
    }
}