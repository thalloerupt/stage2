package com.thallo.stage.utils.filePicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import org.jetbrains.annotations.Nullable

class FilePicker {
    private var uriListener: UriListener? = null
    private lateinit var getContent: ActivityResultLauncher<Boolean>
    var activity: FragmentActivity

    constructor(getContent: ActivityResultLauncher<Boolean>,activity: FragmentActivity) {
        this.getContent=getContent
        this.activity=activity

    }

    fun open(activity: FragmentActivity, mimeTypes: Array<String> ) {
        getContent.launch(true)
        requestPermission()

    }

    fun putUriListener(uriListener: UriListener?) {
        this.uriListener = uriListener
    }

    fun putUri(uri: Uri?) {
        uriListener!!.UriGet(uri)
    }

    // 状态变化监听
    interface UriListener {
        // 回调方法
        fun UriGet(uri: Uri?)
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + activity.getPackageName())
                activity.startActivityForResult(intent, 1024)
                Toast.makeText(activity, "请先授予权限", Toast.LENGTH_SHORT).show()
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf<String>(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1024
                )
            }
        }
    }



}