package com.thallo.stage.utils

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Rect
import android.text.TextPaint
import android.widget.Toast





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


}