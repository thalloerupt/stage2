package com.thallo.stage

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.kongzue.dialogx.DialogX
import com.thallo.stage.webextension.WebextensionSession

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        // apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
        DialogX.init(this);
    }
}