package com.thallo.stage.session

import android.app.Activity
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.thallo.stage.utils.getSizeName
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings
import org.mozilla.geckoview.GeckoSessionSettings

class SeRuSettings {
    private var geckoSessionSettings:GeckoSessionSettings
    private lateinit var geckoRuntimeSettings:GeckoRuntimeSettings
    var activity:Activity

    constructor(
        geckoSessionSettings: GeckoSessionSettings,
        activity:Activity
    ) {
        this.geckoSessionSettings = geckoSessionSettings
        this.activity = activity
        geckoRuntimeSettings=GeckoRuntime.getDefault(activity).settings
        if (getSizeName(activity)=="large"){
            geckoSessionSettings.userAgentOverride="Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0"
            geckoSessionSettings.viewportMode=GeckoSessionSettings.VIEWPORT_MODE_DESKTOP
            geckoSessionSettings.displayMode = GeckoSessionSettings.DISPLAY_MODE_BROWSER
        }
        else {
            geckoSessionSettings.userAgentMode = GeckoSessionSettings.USER_AGENT_MODE_MOBILE

        }
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity /* Activity context */)
        geckoRuntimeSettings.forceUserScalableEnabled=sharedPreferences.getBoolean("switch_userscalable",false)
        geckoRuntimeSettings.automaticFontSizeAdjustment=sharedPreferences.getBoolean("switch_automatic_fontsize",false)
        geckoRuntimeSettings.aboutConfigEnabled=true
        geckoRuntimeSettings.webFontsEnabled=true
        sharedPreferences.registerOnSharedPreferenceChangeListener{ sharedPreferences, key -> when(key){
            "switch_userscalable"->geckoRuntimeSettings.forceUserScalableEnabled=sharedPreferences.getBoolean("switch_userscalable",false)
            "switch_automatic_fontsize"->geckoRuntimeSettings.automaticFontSizeAdjustment=sharedPreferences.getBoolean("switch_automatic_fontsize",false)
        }
        }
    }
}