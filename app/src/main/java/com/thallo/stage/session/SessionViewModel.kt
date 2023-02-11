package com.thallo.stage.session

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.geckoview.GeckoSession

class SessionViewModel() : ViewModel() {
    val currentSession: MutableLiveData<SessionDelegate> by lazy {
        MutableLiveData<SessionDelegate>()
    }
}