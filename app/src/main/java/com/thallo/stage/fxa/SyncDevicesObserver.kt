package com.thallo.stage.fxa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mozilla.components.concept.sync.Device

class SyncDevicesObserver:ViewModel() {
    private val _syncDevicesStateFlow = MutableStateFlow<List<Device>>(emptyList())
    val syncDevicesStateFlow = _syncDevicesStateFlow.asStateFlow()


    fun sendAccountState(devices: List<Device>) {
        viewModelScope.launch {
            _syncDevicesStateFlow.emit(devices)
        }
    }
}