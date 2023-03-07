package com.thallo.stage.utils

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager


object StatusUtils {
    fun init(context: Activity){

        val window: Window = context.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.statusBarColor = Color.TRANSPARENT
        val uiOption = window.decorView.systemUiVisibility
        if (isDarkMode(context)) {
            //没有DARK_STATUS_BAR属性，通过位运算将LIGHT_STATUS_BAR属性去除
            window.decorView.systemUiVisibility =
                uiOption and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        } else {
            //这里是要注意的地方，如果需要补充新的FLAG，记得要带上之前的然后进行或运算
            window.decorView.systemUiVisibility = uiOption or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

    }
    fun hideStatusBar(context: Activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            context.window
                .setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
        }
    }
    fun isDarkMode(context: Activity): Boolean {
        val mode: Int =
            context.getResources().getConfiguration().uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

}