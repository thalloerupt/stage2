package com.thallo.stage

import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors
import com.kongzue.dialogx.DialogX

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        // apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
        DialogX.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        xcrash.XCrash.init(this)



    }
}