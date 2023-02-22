package com.thallo.stage.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.thallo.stage.R
/**
 * 2023.2.11 19:10
 * 正月廿一
 * thallo
 **/
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        findPreference<Preference>("settingAbout")?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_aboutFragment)
            false
        }
        findPreference<Preference>("searching")?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_settingsSearching2)
            false
        }
        findPreference<Preference>("addons")?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_addonsManagerFragment)
            false
        }




    }
}