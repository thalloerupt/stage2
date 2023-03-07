package com.thallo.stage.session

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thallo.stage.BR
import com.thallo.stage.R
import com.thallo.stage.broswer.AlertDialog
import com.thallo.stage.broswer.ConfirmDialog
import com.thallo.stage.broswer.JsChoiceDialog
import com.thallo.stage.broswer.TextDialog
import com.thallo.stage.componets.popup.IntentPopup
import com.thallo.stage.database.history.History
import com.thallo.stage.database.history.HistoryViewModel
import com.thallo.stage.download.DownloadTask
import com.thallo.stage.download.DownloadTaskLiveData
import com.thallo.stage.utils.StatusUtils
import com.thallo.stage.utils.UriUtils
import com.thallo.stage.utils.filePicker.FilePicker
import com.thallo.stage.utils.filePicker.GetFile
import com.thallo.stage.webextension.WebextensionSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mozilla.geckoview.*
import org.mozilla.geckoview.GeckoSession.NavigationDelegate
import org.mozilla.geckoview.GeckoSession.ProgressDelegate
import org.mozilla.geckoview.GeckoSession.PromptDelegate.*


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
    @get:Bindable
    var isFull:Boolean=false

    var downloadTasks=ArrayList<DownloadTask>()
    lateinit var historyViewModel:HistoryViewModel
    lateinit var intentPopup:IntentPopup
    var uri: Uri? =null
    lateinit var filePicker: FilePicker
    constructor(mContext: FragmentActivity, session:GeckoSession, filePicker: FilePicker) : this() {
        this.mContext = mContext
        this.session = session
        this.filePicker=filePicker
        val  geckoViewModel: GeckoViewModel =ViewModelProvider(mContext).get(GeckoViewModel::class.java)
        historyViewModel=ViewModelProvider(mContext).get(HistoryViewModel::class.java)
        bitmap= mContext.getDrawable(R.drawable.logo72)?.toBitmap()!!
        intentPopup =IntentPopup(mContext)


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
                    var downloadTask = DownloadTask(mContext,uri,withContext(Dispatchers.IO){
                            UriUtils.getFileName(uri)
                        })
                    downloadTask.open()
                    downloadTasks.add(downloadTask)
                    DownloadTaskLiveData.getInstance().Value(downloadTasks) }
                }
                Log.d("ExternalResponse","OK")


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
                var history= title?.let { History(u, it,0) }
                historyViewModel.insertHistories(history)

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
                if (enabled) {
                    if (meta != null) {
                        if (meta.height > meta.width) {
                            mContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        } else mContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
                    StatusUtils.hideStatusBar(mContext)

                } else {
                    mContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    StatusUtils.init(mContext)

                }
                isFull=enabled
                notifyPropertyChanged(BR.full)

            }
        }

        session.progressDelegate = object : ProgressDelegate {
            override fun onSessionStateChange(
                session: GeckoSession,
                sessionState: GeckoSession.SessionState
            ) {
                super.onSessionStateChange(session, sessionState)
            }
            override fun onProgressChange(session: GeckoSession, progress: Int) {
                super.onProgressChange(session, progress)
                if (progress!=100)
                    mProgress=progress
                else mProgress=0


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
            override fun onLoadRequest(
                session: GeckoSession,
                request: NavigationDelegate.LoadRequest
            ): GeckoResult<AllowOrDeny>? {
                val uri = Uri.parse(request.uri)
                if (uri.scheme != null) {
                    if (!uri.scheme!!.contains("https") && !uri.scheme!!.contains("http") && !uri.scheme!!.contains(
                            "about"
                        )
                    ) {
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        if (intent.resolveActivity(mContext.packageManager) != null) {
                            intentPopup.show(intent)
                            Log.d("scheme1", uri.scheme!!)
                        }
                    }
                }
                return GeckoResult.fromValue(AllowOrDeny.ALLOW)
            }
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
                    Log.d("可以？", url)
                }
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


        /*
        session.setAutofillDelegate(new Autofill.Delegate() {
            @Override
            public void onAutofill(@NonNull GeckoSession session, int notification, @Nullable Autofill.Node node) {
                AutofillManager afm = context.getSystemService(AutofillManager.class);
                if(afm!=null){

                }
            }
        });*/

        session.promptDelegate = object : GeckoSession.PromptDelegate {

            override fun onBeforeUnloadPrompt(
                session: GeckoSession,
                prompt: BeforeUnloadPrompt
            ): GeckoResult<PromptResponse>? {
                Log.d("BeforeUnload","its me")
                return super.onBeforeUnloadPrompt(session, prompt)
            }
            override fun onButtonPrompt(
                session: GeckoSession,
                prompt: ButtonPrompt
            ): GeckoResult<PromptResponse>? {
                Log.d("ButtonPrompt","its me")

                return super.onButtonPrompt(session, prompt)
            }
            override fun onPopupPrompt(
                session: GeckoSession,
                prompt: PopupPrompt
            ): GeckoResult<PromptResponse>? {
                Log.d("Popup","its me")

                return super.onPopupPrompt(session, prompt)
            }
            override fun onAuthPrompt(
                session: GeckoSession,
                prompt: AuthPrompt
            ): GeckoResult<PromptResponse>? {
                Log.d("AuthPrompt","its me")

                return super.onAuthPrompt(session, prompt)
            }
            override fun onTextPrompt(
                session: GeckoSession,
                prompt: TextPrompt
            ): GeckoResult<PromptResponse>? {
                val alertDialog = TextDialog(mContext, prompt)
                alertDialog.showDialog()
                Log.d("TextPrompt","its me")

                return GeckoResult.fromValue(alertDialog.dialogResult)

            }
            override fun onRepostConfirmPrompt(
                session: GeckoSession,
                prompt: RepostConfirmPrompt
            ): GeckoResult<PromptResponse>? {
                val confirmPrompt = ConfirmDialog(mContext, prompt)
                confirmPrompt.showDialog()
                Log.d("RepostConfirm","its me")

                return GeckoResult.fromValue(confirmPrompt.dialogResult)
            }

            override fun onFilePrompt(
                session: GeckoSession,
                prompt: FilePrompt
            ): GeckoResult<PromptResponse>? {
                val getFile = GetFile(mContext,filePicker)
                getFile.open(mContext, prompt.mimeTypes)
                return GeckoResult.fromValue(prompt.confirm(mContext, getFile.uri))
            }


            override fun onChoicePrompt(
                session: GeckoSession,
                prompt: ChoicePrompt
            ): GeckoResult<PromptResponse>? {
                //prompt.
                val jsChoiceDialog = JsChoiceDialog(mContext, prompt)
                jsChoiceDialog.showDialog()
                Log.d("ButtonPrompt","its me")

                return GeckoResult.fromValue(prompt.confirm(jsChoiceDialog.dialogResult.toString()))
            }

            override fun onAlertPrompt(
                session: GeckoSession,
                prompt: AlertPrompt
            ): GeckoResult<PromptResponse>? {
                val alertDialog = AlertDialog(mContext, prompt)
                alertDialog.showDialog()
                Log.d("ButtonPrompt","its me")

                return GeckoResult.fromValue(alertDialog.dialogResult)
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






