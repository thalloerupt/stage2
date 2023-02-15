package com.thallo.stage.database.history

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.thallo.stage.database.StageData.Companion.getDatabase

internal class HistoryRepository(context: Context) {
    val allHistoriesLive: LiveData<List<History?>?>?
    private val historyDao: HistoryDao?

    init {
        val stageData = getDatabase(context.applicationContext)
        historyDao = stageData!!.historyDao
        allHistoriesLive = historyDao?.allHistoriesLive
    }

    fun insertHistory(vararg histories: History?) {
        InsertAsyncTask(historyDao).execute(*histories)
    }

    fun updateHistory(vararg histories: History?) {
        UpdateAsyncTask(historyDao).execute(*histories)
    }

    fun deleteHistory(vararg histories: History?) {
        DeleteAsyncTask(historyDao).execute(*histories)
    }

    fun deleteAllHistories(vararg histories: History?) {
        DeleteAllAsyncTask(historyDao).execute()
    }

    fun findHistoriesWithPattern(pattern: String): LiveData<List<History?>?>? {
        return historyDao!!.findHistoriesWithPattern("%$pattern%")
    }

    fun findHistoriesWithTitle(pattern: String?): LiveData<List<History?>?>? {
        return historyDao!!.findHistoriesWithTitle(pattern)
    }

    fun findHistoriesWithMix(pattern: String?): LiveData<List<History?>?>? {
        return historyDao!!.findHistoriesWithMix(pattern)
    }

    internal class InsertAsyncTask(private val historyDao: HistoryDao?) :
        AsyncTask<History?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: History?): Void? {
            historyDao!!.insertHistory(*params)
            return null
        }
    }

    internal class UpdateAsyncTask(private val historyDao: HistoryDao?) :
        AsyncTask<History?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: History?): Void? {
            historyDao!!.updateHistory(*params)
            return null
        }
    }

    internal class DeleteAsyncTask(private val historyDao: HistoryDao?) :
        AsyncTask<History?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: History?): Void? {
            historyDao!!.deleteHistory(*params)
            return null
        }
    }

    internal class DeleteAllAsyncTask(private val historyDao: HistoryDao?) :
        AsyncTask<Void?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: Void?): Void? {
            historyDao!!.deleteAllHistories()
            return null
        }
    }
}