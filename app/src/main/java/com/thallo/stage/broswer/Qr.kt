package com.thallo.stage.broswer

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.bingoogolapple.qrcode.zbar.ZBarView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thallo.stage.MainActivity
import com.thallo.stage.R
import com.thallo.stage.session.createSession
import com.thallo.stage.utils.Utils.copyToClipboard

class Qr {
    fun show(activity: MainActivity) {
        val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialog)
        val popView: View = LayoutInflater.from(activity).inflate(R.layout.popup_qr, null)
        val zXingView: ZBarView = popView.findViewById(R.id.zxingview)
        val imageView = popView.findViewById<ImageView>(R.id.qrFlash)
        bottomSheetDialog.setOnDismissListener { zXingView.onDestroy() }
        object : Thread() {
            override fun run() {
                super.run()
                zXingView.startCamera()
                zXingView.startSpot()
            }
        }.start()
        imageView.setOnClickListener(object : View.OnClickListener {
            var a = false
            override fun onClick(view: View) {
                if (!a) {
                    imageView.setImageResource(R.drawable.ic_bulb)
                    a = true
                    zXingView.openFlashlight()
                } else {
                    imageView.setImageResource(R.drawable.ic_bulb_off)
                    a = false
                    zXingView.closeFlashlight()
                }
            }
        })
        zXingView.setDelegate(object : QRCodeView.Delegate {
            override fun onScanQRCodeSuccess(result: String?) {
                if (Patterns.WEB_URL.matcher(result).matches() || URLUtil.isValidUrl(result)){
                    createSession(result,activity)
                }
                else{
                    if (result != null) {
                        dialog(activity,result)
                    }

                }


                bottomSheetDialog.dismiss()
            }

            override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {}
            override fun onScanQRCodeOpenCameraError() {}
        })
        bottomSheetDialog.setContentView(popView)
        bottomSheetDialog.show()
    }
    private fun dialog(context: Context, result: String){
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.result_qr))
            .setMessage("${context.getString(R.string.result_qr)}:$result")
            .setNegativeButton(context.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(context.getString(R.string.copy)) { dialog, which ->
                copyToClipboard(context,result)
            }
            .show()
    }
}