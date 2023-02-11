package com.thallo.stage.download

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.thallo.stage.BR
import com.thallo.stage.utils.UriUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import rxhttp.RxHttpPlugins
import rxhttp.toDownloadFlow
import rxhttp.wrapper.param.RxHttp

class DownloadTask : BaseObservable {
    @get:Bindable
    var title:String=""
    @get:Bindable
    var currentSize: Long = 0
    @get:Bindable
    var totalSize: Long = 0
    @get:Bindable
    var progress: Int = 0
    @get:Bindable
    var state:Int = 0
    @get:Bindable
    var text:String=""

    private var mContext:Context
    var uri:String
    private  var downloadFactory:Android10DownloadFactory
    private var filename:String
    constructor(mContext: Context, uri: String,filename:String)  {
        this.mContext = mContext
        this.uri = uri
        this.filename=filename
        title=filename
        notifyPropertyChanged(BR.title)
        downloadFactory= Android10DownloadFactory(mContext,filename)

    }
    fun open(){
        (mContext as FragmentActivity).lifecycleScope.launch {
            RxHttp.get(uri)
                .tag(uri)
                .toDownloadFlow(downloadFactory,true) {
                    progress = it.progress //当前进度 0-100
                    currentSize = it.currentSize/1024/1024 //当前已下载的字节大小
                    totalSize = it.totalSize/1024/1024 //要下载的总字节大小
                    android.util.Log.d("下载进度",""+progress)
                    text="已下载$currentSize MB•共$totalSize MB"
                    notifyChange()
                }.catch {
                }.collect {
                }

        }
        state=1
        notifyPropertyChanged(BR.state)

    }
    fun pause(){
        RxHttpPlugins.cancelAll(uri)
        state=0
        notifyPropertyChanged(BR.state)
    }
}