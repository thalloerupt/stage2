package com.thallo.stage.database.shortcut

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ShortcutViewModel(application: Application) : AndroidViewModel(application) {
    private val shortcutRepository: ShortcutRepository

    init {
        shortcutRepository = ShortcutRepository(application)
    }

    val allShortcutsLive: LiveData<List<Shortcut?>?>?
        get() = shortcutRepository.allShortcutsLive

    fun findShortcutsWithPattern(pattern: String): LiveData<List<Shortcut?>?>? {
        return shortcutRepository.findShortcutsWithPattern(pattern)
    }

    fun findShortcutsWithTitle(pattern: String?): LiveData<List<Shortcut?>?>? {
        return shortcutRepository.findShortcutsWithTitle(pattern)
    }

    fun findShortcutsWithMix(pattern: String?): LiveData<List<Shortcut?>?>? {
        return shortcutRepository.findShortcutsWithMix(pattern)
    }

    fun insertShortcuts(vararg shortcuts: Shortcut?) {
        shortcutRepository.insertShortcut(*shortcuts)
    }

    fun updateShortcuts(vararg shortcuts: Shortcut?) {
        shortcutRepository.updateShortcut(*shortcuts)
    }

    fun deleteShortcuts(vararg shortcuts: Shortcut?) {
        shortcutRepository.deleteShortcut(*shortcuts)
    }

    fun deleteAllShortcuts() {
        shortcutRepository.deleteAllShortcuts()
    }
}