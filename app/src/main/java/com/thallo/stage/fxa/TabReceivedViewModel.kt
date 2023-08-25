package com.thallo.stage.fxa

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import mozilla.components.concept.sync.Device
import mozilla.components.concept.sync.DeviceType
import mozilla.components.concept.sync.TabData

class TabReceivedViewModel: ViewModel() {
    private var _tabs = MutableStateFlow<List<TabData>>(emptyList())
    private var _device = MutableStateFlow<Device>(Device(
        null.toString(),
        null.toString(),
        DeviceType.MOBILE,
        null == true,
        null,
        emptyList(),
        null == true,
        null,))
    val tabs = _tabs.asStateFlow()
    val device = _device.asStateFlow()
    fun changeTabs(device: Device,tabs: List<TabData>) {
        _device.value = device
        _tabs.value = tabs
    }
}