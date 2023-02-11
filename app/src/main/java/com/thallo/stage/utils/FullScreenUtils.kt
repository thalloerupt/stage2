package com.thallo.stage.utils

import android.app.Activity
import android.view.View
import android.view.WindowManager


/**@RequiresApi(Build.VERSION_CODES.R)
fun ScreenUtils(context: Activity) {
    context.window.insetsController?.hide(WindowInsets.Type.statusBars())
    context.window.insetsController?.hide(WindowInsets.Type.navigationBars())

}**/
fun FullScreen(context: Activity)
{
    context.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    val decorView: View = context.getWindow().getDecorView()
    val uiOptions: Int =
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
    decorView.setSystemUiVisibility(uiOptions)
    decorView.setOnSystemUiVisibilityChangeListener(object :
        View.OnSystemUiVisibilityChangeListener {
        override fun onSystemUiVisibilityChange(i: Int) {
            if (i and View.SYSTEM_UI_FLAG_FULLSCREEN === 0) {
                decorView.setSystemUiVisibility(uiOptions)
            } else {
            }
        }
    })
}