package com.thallo.stage.componets.popup

import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thallo.stage.HolderActivity
import com.thallo.stage.MainActivity
import com.thallo.stage.R
import com.thallo.stage.componets.MenuAddonsAdapater
import com.thallo.stage.database.bookmark.Bookmark
import com.thallo.stage.database.bookmark.BookmarkViewModel
import com.thallo.stage.databinding.PopupMenuBinding
import com.thallo.stage.session.DelegateLivedata
import com.thallo.stage.session.SessionDelegate
import org.mozilla.geckoview.GeckoRuntime

class MenuPopup{
    val context: MainActivity
    private var bottomSheetDialog:BottomSheetDialog
    var binding: PopupMenuBinding
    private var bookmarkViewModel: BookmarkViewModel
    private  var sessionDelegate: SessionDelegate? =null
    constructor(
        context: MainActivity,
    ) {
        this.context = context
        bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog )
        bookmarkViewModel = ViewModelProvider(context).get<BookmarkViewModel>(BookmarkViewModel::class.java)
        binding = PopupMenuBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(binding.root)
        DelegateLivedata.getInstance().observe(context){sessionDelegate=it}
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
        binding.starButton.setOnClickListener {
            if (sessionDelegate!=null){
                var bookmark=Bookmark(sessionDelegate!!.u, sessionDelegate!!.mTitle,"默认",true)
                bookmarkViewModel.insertBookmarks(bookmark)
            }
        }

        var adapter= MenuAddonsAdapater()
        binding.menuAddonsRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.menuAddonsRecyclerView.adapter=adapter
        GeckoRuntime.getDefault(context).webExtensionController.list().accept {
            adapter.submitList(it)
        }

    }
    fun show(){
        if (sessionDelegate!=null)
            binding.textView5.text= sessionDelegate!!.mTitle
        bottomSheetDialog.show()
    }
}