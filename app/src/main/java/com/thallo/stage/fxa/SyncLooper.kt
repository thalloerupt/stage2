package com.thallo.stage.fxa

import java.lang.Error

data class SyncState(
    val error: Boolean = false,
    val idle:Boolean = false,
    val start:Boolean = false
    ):AccountState

sealed class SyncLooper {
}