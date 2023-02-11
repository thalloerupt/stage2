package com.thallo.stage.utils

import android.content.Context
import android.content.res.Configuration

fun getSizeName(context: Context): String? {
    var screenLayout = context.resources.configuration.screenLayout
    screenLayout = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
    return when (screenLayout) {
        Configuration.SCREENLAYOUT_SIZE_SMALL -> "small"
        Configuration.SCREENLAYOUT_SIZE_NORMAL -> "normal"
        Configuration.SCREENLAYOUT_SIZE_LARGE -> "large"
        4 -> "xlarge"
        else -> "undefined"
    }
}