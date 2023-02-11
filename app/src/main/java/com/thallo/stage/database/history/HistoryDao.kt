package com.thallo.stage.database.history

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    //添加用户
    @Insert
    fun addHistory(vararg History: History)
    //查找用户
    //返回History表中所有的数据,使用LiveData
    @Query("select * from History")
    fun getHistoryData(): LiveData<List<History>>
}