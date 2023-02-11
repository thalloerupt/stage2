package com.thallo.stage.download

import androidx.lifecycle.LiveData
import com.thallo.stage.session.DelegateLivedata
import com.thallo.stage.session.SessionDelegate

class DownloadTaskLiveData : LiveData<ArrayList<DownloadTask>>() {
    fun Value(downloadTasks: ArrayList<DownloadTask>){
        postValue(downloadTasks)
    }
    override fun onActive() {
        super.onActive()
    }

    override fun onInactive() {
        super.onInactive()
    }
    companion object {
        private lateinit var globalData: DownloadTaskLiveData
        fun getInstance(): DownloadTaskLiveData {
            globalData = if (Companion::globalData.isInitialized) globalData else DownloadTaskLiveData()
            return globalData
        }
    }
}