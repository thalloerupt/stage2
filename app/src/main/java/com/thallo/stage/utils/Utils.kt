package com.thallo.stage.utils

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.media.AudioAttributes
import android.os.Build
import android.os.Vibrator
import android.text.TextPaint
import android.widget.Toast
import androidx.compose.ui.graphics.Color


/**
 * https://github.com/shehuan/GroupIndexLib
 */


object Utils {
    //根据手机的分辨率从dp的单位转成px（像素）
    fun dip2px(context: Context, dpValue: Int): Int {
        //获取当前手机的像素密度(1个dp对应几个px)
        val scale = context.resources.displayMetrics.density
        //四舍五入取整
        return (dpValue * scale + 0.5f).toInt()
    }
    fun sp2px(context: Context, spValue: Int): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 测量字符高度
     *
     * @param text
     * @return
     */
    fun getTextHeight(textPaint: TextPaint, text: String): Int {
        val bounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, bounds)
        return bounds.height()
    }
    fun isPortraitMode(context: Context) : Boolean{
        val mConfiguration: Configuration = context.resources.configuration //获取设置的配置信息
        return mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT
    }
    /**
     * 测量字符宽度
     *
     * @param textPaint
     * @param text
     * @return
     */
    fun getTextWidth(textPaint: TextPaint, text: String?): Int {
        return textPaint.measureText(text).toInt()
    }

    fun listIsEmpty(list: List<String?>?): Boolean {
        return list == null || list.size == 0
    }

    @SuppressLint("ServiceCast")
    fun copyToClipboard(context: Context, content: String?) {
        // 从 API11 开始 android 推荐使用 android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的 android.text.ClipboardManager，虽然提示 deprecated，但不影响使用。
        val cm: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 将文本内容放到系统剪贴板里。
        cm.setText(content)
        Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show()
    }

    fun Int.requireColor(context: Context): androidx.compose.ui.graphics.Color = Color(context.getColor(this))

    /**
     * 手机震动
     *
     * @param context
     * @param isRepeat 是否重复震动
     */
    fun playVibrate(context: Context, isRepeat: Boolean) {

        try {
            val mVibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            val patern = longArrayOf(100,200,100)
            var audioAttributes: AudioAttributes? = null
            /**
             * 适配android7.0以上版本的震动
             * 说明：如果发现5.0或6.0版本在app退到后台之后也无法震动，那么只需要改下方的Build.VERSION_CODES.N版本号即可
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM) //key
                    .build()
                mVibrator.vibrate(patern, if (isRepeat) 1 else -1, audioAttributes)
            } else {
                mVibrator.vibrate(patern, if (isRepeat) 1 else -1)
            }
        } catch (ex: Exception) {
        }
    }


}