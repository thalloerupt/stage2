package com.thallo.stage.broswer

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.bingoogolapple.qrcode.zbar.ZBarView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thallo.stage.MainActivity
import com.thallo.stage.R

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
                bottomSheetDialog.dismiss()
            }

            override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {}
            override fun onScanQRCodeOpenCameraError() {}
        })
        bottomSheetDialog.setContentView(popView)
        bottomSheetDialog.show()
    }
}