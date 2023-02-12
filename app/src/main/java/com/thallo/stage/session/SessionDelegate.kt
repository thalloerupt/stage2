package com.thallo.stage.session

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thallo.stage.*
import com.thallo.stage.R
import com.thallo.stage.download.DownloadTask
import com.thallo.stage.download.DownloadTaskLiveData
import com.thallo.stage.utils.UriUtils
import com.thallo.stage.webextension.WebextensionSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mozilla.geckoview.*
import org.mozilla.geckoview.GeckoSession.NavigationDelegate
import org.mozilla.geckoview.GeckoSession.ProgressDelegate


class SessionDelegate() :BaseObservable(){


    lateinit var session:GeckoSession
    private lateinit var mContext: FragmentActivity
    lateinit var login: Login
    lateinit var setpic: Setpic
    val CONFIG_URL = "https://accounts.firefox.com"
    @get:Bindable
    var u: String = ""
    @get:Bindable
    lateinit var bitmap: Bitmap
    @get:Bindable
    var mTitle: String = ""
    @get:Bindable
    var active:Boolean = false
        set(value) {
            field = value
            // 只刷新当前属性
            notifyPropertyChanged(BR.active)
        }
    @get:Bindable
    var mProgress:Int=0
    @get:Bindable
    var canBack:Boolean=false
    @get:Bindable
    var canForward:Boolean=false

    var downloadTasks=ArrayList<DownloadTask>()

    constructor(mContext: FragmentActivity, session:GeckoSession) : this() {
        this.mContext = mContext
        this.session = session
        val  geckoViewModel: GeckoViewModel =ViewModelProvider(mContext).get(GeckoViewModel::class.java)
        val  sessionViewModel: SessionViewModel =ViewModelProvider(mContext).get(SessionViewModel::class.java)
        bitmap= mContext.getDrawable(R.drawable.home_outline)?.toBitmap()!!
        DownloadTaskLiveData.getInstance().observe(mContext){
            downloadTasks=it
        }

        session.contentDelegate = object : GeckoSession.ContentDelegate {
            override fun onExternalResponse(session: GeckoSession, response: WebResponse) {
                var uri=response.uri
                if (uri.endsWith("xpi")){
                    WebextensionSession(mContext).install(uri)
                }
                else{
                mContext.lifecycleScope.launch {
                    var downloadTask:DownloadTask = DownloadTask(mContext,uri,withContext(Dispatchers.IO){
                            UriUtils().getFileName(uri)
                        })
                    downloadTask.open()
                    downloadTasks.add(downloadTask)
                    DownloadTaskLiveData.getInstance().Value(downloadTasks) }
                }


            }
            override fun onPaintStatusReset(session: GeckoSession) {
                setpic.onSetPic()
                notifyPropertyChanged(BR.bitmap)
            }
            override fun onFirstComposite(session: GeckoSession) {
                setpic.onSetPic()
                notifyPropertyChanged(BR.bitmap)
            }
            override fun onTitleChange(session: GeckoSession, title: String?) {
                if (title != null) {
                    mTitle=title
                }
                notifyPropertyChanged(BR.mTitle)

            }
        }


        session.mediaSessionDelegate = object : MediaSession.Delegate {
            var orientation: Boolean? = null
            override fun onFullscreen(
                session: GeckoSession,
                mediaSession: MediaSession,
                enabled: Boolean,
                meta: MediaSession.ElementMetadata?
            ) {


            }
        }

        session.progressDelegate = object : ProgressDelegate {
            override fun onProgressChange(session: GeckoSession, progress: Int) {
                super.onProgressChange(session, progress)
                mProgress=progress
                notifyPropertyChanged(BR.mProgress)
            }
            override fun onPageStart(session: GeckoSession, url: String) {
                if (url.startsWith("$CONFIG_URL/oauth/success/3c49430b43dfba77")) {
                    val uri = Uri.parse(url)
                    val code = uri.getQueryParameter("code")
                    val state = uri.getQueryParameter("state")
                    val action = uri.getQueryParameter("action")
                    if (code != null && state != null&& action != null) {
                        //listener?.onLoginComplete(code, state, mContext)
                        //Toast.makeText(mContext,code+"**"+state,Toast.LENGTH_SHORT).show()
                            login.onLogin(code,state,action)
                    }
                }

            }
            override fun onPageStop(session: GeckoSession, success: Boolean) {
            }
        }


        session.navigationDelegate = object : NavigationDelegate {
            override fun onLoadError(
                session: GeckoSession,
                uri: String?,
                error: WebRequestError
            ): GeckoResult<String>? {
                return super.onLoadError(session, uri, error)
            }
            override fun onCanGoForward(session: GeckoSession, canGoForward: Boolean) {
                super.onCanGoForward(session, canGoForward)
                canForward=canGoForward
                notifyPropertyChanged(BR.canForward)
            }
            override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
                super.onCanGoBack(session, canGoBack)
                canBack=canGoBack
                notifyPropertyChanged(BR.canBack)
            }
            override fun onLocationChange(
                session: GeckoSession,
                url: String?,
                perms: MutableList<GeckoSession.PermissionDelegate.ContentPermission>
            ) {

                super.onLocationChange(session, url, perms)
                if (url != null) {
                    u=url
                }

                notifyPropertyChanged(com.thallo.stage.BR.u)
            }
            override fun onNewSession(
                session: GeckoSession,
                uri: String
            ): GeckoResult<GeckoSession>? {
                val newSession = GeckoSession()
                geckoViewModel.changeSearch(newSession)
                return GeckoResult.fromValue(newSession)
            }
        }





    }

    fun close(){
        session.close()
        bitmap.recycle()
    }
    fun open(){
        if(!session.isOpen)
            session.open(GeckoRuntime.getDefault(mContext))
    }

    interface Login{
        fun onLogin(code:String,state:String,action:String)
    }
    interface Setpic{
        fun onSetPic()
    }

    }




