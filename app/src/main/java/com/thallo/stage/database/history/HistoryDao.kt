package com.thallo.stage.database.history

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    @Insert
    fun insertHistory(vararg histories: History?)

    @Update
    fun updateHistory(vararg histories: History?)

    @Delete
    fun deleteHistory(vararg histories: History?)

    @Query("DELETE FROM HISTORY")
    fun deleteAllHistories()

    @get:Query("SELECT * FROM HISTORY ORDER BY ID DESC")
    val allHistoriesLive: LiveData<List<History?>?>?

    @Query("SELECT * FROM History WHERE url_info LIKE:pattern ORDER BY ID DESC")
    fun findHistoriesWithPattern(pattern: String?): LiveData<List<History?>?>?

    @Query("SELECT * FROM History WHERE title_info LIKE:pattern ORDER BY ID DESC")
    fun findHistoriesWithTitle(pattern: String?): LiveData<List<History?>?>?

    @Query("SELECT * FROM History WHERE mix LIKE:pattern ORDER BY ID DESC")
    fun findHistoriesWithMix(pattern: String?): LiveData<List<History?>?>?
}