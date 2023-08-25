package com.thallo.stage.fxa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountStateViewModel :ViewModel(){
    private val _accountStateFlow = MutableStateFlow(SyncState())
    val accountStateFlow = _accountStateFlow.asStateFlow()


    fun sendAccountState(syncState:SyncState) {
        viewModelScope.launch {
            _accountStateFlow.emit(syncState)
        }
    }
}