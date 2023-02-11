package com.thallo.stage.componets

import androidx.lifecycle.LiveData
import com.thallo.stage.tab.AddTabLiveData

class HomeLivedata: LiveData<Boolean>() {
    fun Value(boolean: Boolean){
        postValue(boolean)
    }
    override fun onActive() {
        super.onActive()
    }

    override fun onInactive() {
        super.onInactive()
    }
    companion object {
        private lateinit var globalData: HomeLivedata
        fun getInstance(): HomeLivedata {
            globalData = if (Companion::globalData.isInitialized) globalData else HomeLivedata()
            return globalData
        }
    }
}