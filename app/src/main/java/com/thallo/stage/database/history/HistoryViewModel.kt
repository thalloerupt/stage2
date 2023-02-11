package com.thallo.stage.database.history

import androidx.lifecycle.ViewModel

class HistoryViewModel (historyDao: HistoryDao): ViewModel(){
    var historyData=historyDao.getHistoryData()
}