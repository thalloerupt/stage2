package com.thallo.stage.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.thallo.stage.R

class SettingsSearching : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.searching_preferences, rootKey)
    }
}