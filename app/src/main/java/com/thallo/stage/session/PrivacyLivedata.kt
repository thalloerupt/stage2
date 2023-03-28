package com.thallo.stage.session

import androidx.lifecycle.LiveData

class PrivacyLivedata : LiveData<Boolean>() {
    fun Value(privacy: Boolean){
        postValue(privacy)
    }
    override fun onActive() {
        super.onActive()
    }

    override fun onInactive() {
        super.onInactive()
    }
    companion object {
        private lateinit var globalData: PrivacyLivedata
        fun getInstance(): PrivacyLivedata {
            globalData = if (Companion::globalData.isInitialized) globalData else PrivacyLivedata()
            return globalData
        }
    }
}