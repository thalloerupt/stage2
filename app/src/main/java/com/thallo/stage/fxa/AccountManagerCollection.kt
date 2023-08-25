package com.thallo.stage.fxa

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import mozilla.components.service.fxa.manager.FxaAccountManager

class AccountManagerCollection : ViewModel() {
    private var _data = MutableSharedFlow<FxaAccountManager>(replay = 1)
    val data = _data.asSharedFlow()

    fun change(fxaAccountManager: FxaAccountManager){
        _data.tryEmit(fxaAccountManager)
    }

}