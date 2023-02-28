package com.thallo.stage.broswer

import android.app.Activity
import androidx.preference.PreferenceManager

fun SearchEngine(activity: Activity):String {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity /* Activity context */)
    var engine:String
    engine = if (sharedPreferences.getBoolean("switch_diy",false))
        sharedPreferences.getString("edit_diy","").toString()
    else
        sharedPreferences.getString("searchEngine","").toString()
    sharedPreferences.registerOnSharedPreferenceChangeListener{ sharedPreferences, key ->
        engine = if (sharedPreferences.getBoolean("switch_diy",false))
            sharedPreferences.getString("edit_diy","").toString()
        else
            sharedPreferences.getString("searchEngine","").toString()
    }
    return engine
}