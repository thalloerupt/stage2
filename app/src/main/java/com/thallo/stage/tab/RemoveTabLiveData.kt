package com.thallo.stage.tab

import androidx.lifecycle.LiveData
import com.thallo.stage.session.DelegateLivedata
import com.thallo.stage.session.SessionDelegate

class RemoveTabLiveData : LiveData<Int>() {
    fun Value(int: Int){
        postValue(int)
    }
    override fun onActive() {
        super.onActive()
    }

    override fun onInactive() {
        super.onInactive()
    }
    companion object {
        private lateinit var globalData: RemoveTabLiveData
        fun getInstance(): RemoveTabLiveData {
            globalData = if (Companion::globalData.isInitialized) globalData else RemoveTabLiveData()
            return globalData
        }
    }
}