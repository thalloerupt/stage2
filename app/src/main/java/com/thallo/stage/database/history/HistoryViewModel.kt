package com.thallo.stage.database.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val historyRepository: HistoryRepository

    init {
        historyRepository = HistoryRepository(application)
    }

    val allHistoriesLive: LiveData<List<History?>?>?
        get() = historyRepository.allHistoriesLive

    fun findHistoriesWithPattern(pattern: String): LiveData<List<History?>?>? {
        return historyRepository.findHistoriesWithPattern(pattern)
    }

    fun findHistoriesWithTitle(pattern: String?): LiveData<List<History?>?>? {
        return historyRepository.findHistoriesWithTitle(pattern)
    }

    fun findHistoriesWithMix(pattern: String?): LiveData<List<History?>?>? {
        return historyRepository.findHistoriesWithMix(pattern)
    }

    fun insertHistories(vararg histories: History?) {
        historyRepository.insertHistory(*histories)
    }

    fun updateHistories(vararg histories: History?) {
        historyRepository.updateHistory(*histories)
    }

    fun deleteHistories(vararg histories: History?) {
        historyRepository.deleteHistory(*histories)
    }

    fun deleteAllHistories() {
        historyRepository.deleteAllHistories()
    }
}