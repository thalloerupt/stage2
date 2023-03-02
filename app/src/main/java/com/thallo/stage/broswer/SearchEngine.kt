package com.thallo.stage.broswer

import android.app.Activity
import androidx.preference.PreferenceManager
import com.thallo.stage.R

fun SearchEngine(activity: Activity):String {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity /* Activity context */)
    var engine:String
    engine = if (sharedPreferences.getBoolean("switch_diy",false))
        sharedPreferences.getString("edit_diy","").toString()
    else
        sharedPreferences.getString("searchEngine",activity.getString(R.string.baidu)).toString()
    sharedPreferences.registerOnSharedPreferenceChangeListener{ sharedPreferences, _ ->
        engine = if (sharedPreferences.getBoolean("switch_diy",false))
            sharedPreferences.getString("edit_diy","").toString()
        else
            sharedPreferences.getString("searchEngine",activity.getString(R.string.baidu)).toString()
    }
    return engine
}