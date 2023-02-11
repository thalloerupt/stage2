package com.thallo.stage.database.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thallo.stage.database.history.HistoryDao
import com.thallo.stage.database.history.HistoryViewModel

class HistoryViewModelFactory(private val historyDao: HistoryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(historyDao) as T
    }
}