package com.thallo.stage.settings

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.thallo.stage.MainActivity
import com.thallo.stage.R
import com.thallo.stage.session.createSession

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
        findPreference<Preference>("privacyAndService")?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_privacyAndServiceFragment)
            false
        }
        findPreference<Preference>("settingDonating")?.setOnPreferenceClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)

            if ((getResources().getConfiguration().screenLayout and
                        Configuration.SCREENLAYOUT_SIZE_MASK) ===
                Configuration.SCREENLAYOUT_SIZE_LARGE)
            {
                createSession("https://afdian.net/a/stagebrowser",requireActivity())
            }else {
                intent.data = Uri.parse("https://afdian.net/a/stagebrowser")
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            false
        }
        preferenceScreen.onPreferenceClickListener = Preference.OnPreferenceClickListener { true }



    }
}