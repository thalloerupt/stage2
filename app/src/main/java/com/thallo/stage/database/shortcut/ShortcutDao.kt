package com.thallo.stage.database.shortcut

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShortcutDao {
    @Insert
    fun insertShortcut(vararg shortcuts: Shortcut?)

    @Update
    fun updateShortcut(vararg shortcuts: Shortcut?)

    @Delete
    fun deleteShortcut(vararg shortcuts: Shortcut?)

    @Query("DELETE FROM Shortcut")
    fun deleteAllShortcuts()

    @get:Query("SELECT * FROM Shortcut ORDER BY ID DESC")
    val allShortcutsLive: LiveData<List<Shortcut?>?>?

    @Query("SELECT * FROM Shortcut WHERE url_info LIKE:pattern ORDER BY ID DESC")
    fun findShortcutsWithPattern(pattern: String?): LiveData<List<Shortcut?>?>?

    @Query("SELECT * FROM Shortcut WHERE title_info LIKE:pattern ORDER BY ID DESC")
    fun findShortcutsWithTitle(pattern: String?): LiveData<List<Shortcut?>?>?

    @Query("SELECT * FROM Shortcut WHERE mix LIKE:pattern ORDER BY ID DESC")
    fun findShortcutsWithMix(pattern: String?): LiveData<List<Shortcut?>?>?
}