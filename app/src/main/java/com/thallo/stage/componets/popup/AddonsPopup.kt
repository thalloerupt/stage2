package com.thallo.stage.componets.popup

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thallo.stage.HolderActivity
import com.thallo.stage.MainActivity
import com.thallo.stage.R
import com.thallo.stage.databinding.PopupAddonsBinding
import com.thallo.stage.databinding.PopupMenuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.WebExtension

class AddonsPopup {
    val context: Context
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var binding: PopupAddonsBinding
    constructor(
        context: Context,
    ) {
        this.context = context
        bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog )
        binding = PopupAddonsBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.behavior.isDraggable=false
        binding.button.setOnClickListener { bottomSheetDialog.dismiss()}


    }
    fun show(session:GeckoSession, extension: WebExtension){
        session.open(GeckoRuntime.getDefault(context))
        context as LifecycleOwner
        context.lifecycleScope.launch {
            extension.metaData.icon.getBitmap(72).accept { binding.imageView5.setImageBitmap(it) }

            binding.textView.text=extension.metaData.name
        }
        binding.addonsView.setSession(session)
        bottomSheetDialog.show()
    }
}