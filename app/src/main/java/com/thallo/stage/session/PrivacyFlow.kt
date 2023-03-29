package com.thallo.stage.session

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PrivacyFlow : ViewModel() {
    private var b:Boolean = false
    private var _data = MutableStateFlow<Boolean>(b)
    val data = _data.asStateFlow()
    fun changeMode(b:Boolean) {
        _data.value = b
    }
}