package com.thallo.stage.session

import androidx.lifecycle.LiveData

class DelegateLivedata : LiveData<SessionDelegate>() {
    fun Value(sessionDelegate: SessionDelegate){
        postValue(sessionDelegate)
    }
    override fun onActive() {
        super.onActive()
    }

    override fun onInactive() {
        super.onInactive()
    }
    companion object {
        private lateinit var globalData: DelegateLivedata
        fun getInstance(): DelegateLivedata {
            globalData = if (Companion::globalData.isInitialized) globalData else DelegateLivedata()
            return globalData
        }
    }
}