package com.thallo.stage.utils

import android.content.Context


object DpUtils {
    //根据手机的分辨率从dp的单位转成px（像素）
    fun dip2px(context: Context, dpValue: Float): Int {
        //获取当前手机的像素密度(1个dp对应几个px)
        val scale = context.resources.displayMetrics.density
        //四舍五入取整
        return (dpValue * scale + 0.5f).toInt()
    }
}