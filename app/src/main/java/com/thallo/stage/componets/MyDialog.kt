package com.thallo.stage.componets

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.thallo.stage.R


open class MyDialog(context: Context) : AlertDialog(context) {
    init {
        window!!.setBackgroundDrawable(context.getDrawable(R.drawable.bg_dialog))
    }
}