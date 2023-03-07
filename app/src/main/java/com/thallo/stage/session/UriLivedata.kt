package com.thallo.stage.session

import android.net.Uri
import androidx.lifecycle.LiveData

class UriLivedata : LiveData<Uri>() {
    fun Value(uri: Uri){
        postValue(uri)
    }
    override fun onActive() {
        super.onActive()
    }

    override fun onInactive() {
        super.onInactive()
    }
    companion object {
        private lateinit var globalData: UriLivedata
        fun getInstance(): UriLivedata {
            globalData = if (Companion::globalData.isInitialized) globalData else UriLivedata()
            return globalData
        }
    }
}