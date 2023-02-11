package com.thallo.stage.tab

import androidx.lifecycle.LiveData
import com.thallo.stage.session.DelegateLivedata
import com.thallo.stage.session.SessionDelegate

class AddTabLiveData : LiveData<Int>() {
    fun Value(i: Int){
        postValue(i)
    }
    override fun onActive() {
        super.onActive()
    }

    override fun onInactive() {
        super.onInactive()
    }
    companion object {
        private lateinit var globalData: AddTabLiveData
        fun getInstance(): AddTabLiveData {
            globalData = if (Companion::globalData.isInitialized) globalData else AddTabLiveData()
            return globalData
        }
    }
}