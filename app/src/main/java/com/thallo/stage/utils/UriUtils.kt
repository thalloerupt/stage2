package com.thallo.stage.utils

import android.R
import android.content.ContentResolver
import android.net.Uri
import android.webkit.URLUtil
import androidx.annotation.NonNull
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder


object UriUtils {
    fun isUriContentScheme(@NonNull uri: Uri): Boolean {
        return uri.scheme == ContentResolver.SCHEME_CONTENT
    }

    fun isUriFileScheme(@NonNull uri: Uri): Boolean {
        return uri.scheme == ContentResolver.SCHEME_FILE
    }


    fun getFileName(url: String): String {
        var filename=URLUtil.guessFileName(url, null, null)
        filename=filename.substring(0, filename.lastIndexOf("."))+"_${System.currentTimeMillis()}"+filename.substring( filename.lastIndexOf("."))
        return filename
    }


}