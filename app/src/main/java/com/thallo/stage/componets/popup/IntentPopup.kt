package com.thallo.stage.componets.popup

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thallo.stage.R
import com.thallo.stage.databinding.PopupIntentBinding

class IntentPopup {
    var context: Context? = null
    var binding: PopupIntentBinding? = null
    lateinit var bottomSheetDialog: BottomSheetDialog

    constructor(context: Context?) {
        this.context = context
        binding = PopupIntentBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog = BottomSheetDialog(context!!, R.style.BottomSheetDialog)
        bottomSheetDialog.setContentView(binding!!.root)
    }


    fun show(intent: Intent?) {

        bottomSheetDialog.show()
        binding!!.IntentButton.setOnClickListener(View.OnClickListener {
            context!!.startActivity(intent)
            bottomSheetDialog.dismiss()
        })
    }


}