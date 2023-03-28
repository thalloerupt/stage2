package com.thallo.stage.session

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mozilla.geckoview.GeckoSession

class GeckoViewModel:ViewModel() {
    private var _data = MutableStateFlow<GeckoSession>(GeckoSession())
    val data = _data.asStateFlow()
    fun changeSearch(session1: GeckoSession) {
        _data.value = session1
    }
}


