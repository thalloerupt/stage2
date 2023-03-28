package com.thallo.stage.componets.binding

import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.thallo.stage.R
import com.thallo.stage.utils.RoundedCornersTransform
import java.net.URI

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
    if (boolean)
        view.visibility= View.VISIBLE
    else view.visibility= View.GONE
}
@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter(value = ["activeL"], requireAll = false)
fun isActiveL(view: ProgressBar, boolean: Boolean) {
    if (boolean == null) return
    if (boolean)
        view.visibility= View.VISIBLE
    else view.visibility= View.GONE

}

@BindingAdapter(value = ["iconUri"], requireAll = false)
fun loadIcon(view: ImageView, url: String?) {
    if (url == null) return
    val uri = URI.create(url)
    val faviconUrl = uri.scheme + "://" + uri.host + "/favicon.ico"
    Glide.with(view.context).load(faviconUrl).placeholder(R.drawable.globe)
        .into(view)
}
@BindingAdapter(value = ["stateIcon"], requireAll = false)
fun stateIcon(view: MaterialButton, state: Int?) {
    if (state == null) return
    when(state){
        0->view.icon=view.context.getDrawable(R.drawable.play_circle)
        1->view.icon=view.context.getDrawable(R.drawable.pause_circle)
        2->view.icon=view.context.getDrawable(R.drawable.play_circle)

    }

}
@BindingAdapter(value = ["secureIcon"], requireAll = false)
fun secureIcon(view: MaterialButton, isSecure: Boolean?) {
    if (isSecure == null) return
    if (isSecure) view.icon=view.context.getDrawable(R.drawable.shield_fill)
    else view.icon=view.context.getDrawable(R.drawable.shield_slash_fill)
}

@BindingAdapter(value = ["secureText"], requireAll = false)
fun secureButtonText(view: MaterialButton, isSecure: Boolean?) {
    if (isSecure == null) return
    if (isSecure) view.text=view.context.getString(R.string.connection_secure)
    else view.text=view.context.getText(R.string.connection_not_secure)
}
