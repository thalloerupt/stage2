package com.thallo.stage.database.shortcut

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.thallo.stage.database.StageData.Companion.getDatabase

internal class ShortcutRepository(context: Context) {
    val allShortcutsLive: LiveData<List<Shortcut?>?>?
    private val shortcutDao: ShortcutDao?

    init {
        val stageData = getDatabase(context.applicationContext)
        shortcutDao = stageData!!.shortcutDao
        allShortcutsLive = shortcutDao?.allShortcutsLive
    }

    fun insertShortcut(vararg shortcuts: Shortcut?) {
        InsertAsyncTask(shortcutDao).execute(*shortcuts)
    }

    fun updateShortcut(vararg shortcuts: Shortcut?) {
        UpdateAsyncTask(shortcutDao).execute(*shortcuts)
    }

    fun deleteShortcut(vararg shortcuts: Shortcut?) {
        DeleteAsyncTask(shortcutDao).execute(*shortcuts)
    }

    fun deleteAllShortcuts(vararg shortcuts: Shortcut?) {
        DeleteAllAsyncTask(shortcutDao).execute()
    }

    fun findShortcutsWithPattern(pattern: String): LiveData<List<Shortcut?>?>? {
        return shortcutDao!!.findShortcutsWithPattern("%$pattern%")
    }

    fun findShortcutsWithTitle(pattern: String?): LiveData<List<Shortcut?>?>? {
        return shortcutDao!!.findShortcutsWithTitle(pattern)
    }

    fun findShortcutsWithMix(pattern: String?): LiveData<List<Shortcut?>?>? {
        return shortcutDao!!.findShortcutsWithMix(pattern)
    }

    internal class InsertAsyncTask(private val shortcutDao: ShortcutDao?) :
        AsyncTask<Shortcut?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: Shortcut?): Void? {
            shortcutDao!!.insertShortcut(*params)
            return null
        }
    }

    internal class UpdateAsyncTask(private val shortcutDao: ShortcutDao?) :
        AsyncTask<Shortcut?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: Shortcut?): Void? {
            shortcutDao!!.updateShortcut(*params)
            return null
        }
    }

    internal class DeleteAsyncTask(private val shortcutDao: ShortcutDao?) :
        AsyncTask<Shortcut?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: Shortcut?): Void? {
            shortcutDao!!.deleteShortcut(*params)
            return null
        }
    }

    internal class DeleteAllAsyncTask(private val shortcutDao: ShortcutDao?) :
        AsyncTask<Void?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: Void?): Void? {
            shortcutDao!!.deleteAllShortcuts()
            return null
        }
    }
}