package com.thallo.stage.componets.binding

import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.thallo.stage.componets.RoundedCornersTransform

@BindingAdapter(value = ["imageBitmap"], requireAll = false)
    fun loadImage(view: ImageView, bitmap: Bitmap) {
        if (bitmap == null) return
        //默认裁剪四个圆角，不需要设置圆角，对应参数设为false
        Glide.with(view.context)
            .load(bitmap)
            .apply(
                RequestOptions().transform(
                    CenterCrop(), RoundedCornersTransform(
                        view.context, 16f
                    )
                )
            )
            .into(view)
    }

@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter(value = ["active"], requireAll = false)
fun isActive(view: MaterialCardView, boolean: Boolean) {
    if (boolean == null) return
    //默认裁剪四个圆角，不需要设置圆角，对应参数设为false
    if (boolean)
        view.visibility= View.VISIBLE
    else view.visibility= View.GONE
}
@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter(value = ["activeL"], requireAll = false)
fun isActiveL(view: ProgressBar, boolean: Boolean) {
    if (boolean == null) return
    //默认裁剪四个圆角，不需要设置圆角，对应参数设为false
    if (boolean)
        view.visibility= View.VISIBLE
    else view.visibility= View.GONE

}
