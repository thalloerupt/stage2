package com.thallo.stage.webextension

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.thallo.stage.componets.HomeLivedata
import com.thallo.stage.componets.PermissionDialog
import com.thallo.stage.session.DelegateLivedata
import com.thallo.stage.session.GeckoViewModel
import org.mozilla.geckoview.*
import org.mozilla.geckoview.WebExtension.SessionController

class WebextensionSession {
     var context:Activity
     private var webExtensionController:WebExtensionController
     private var webExtensions=ArrayList<WebExtension>()
    constructor(context: Activity) {
        this.context = context
        webExtensionController=GeckoRuntime.getDefault(context).webExtensionController
        webExtensionController.list().accept {
            if (it != null) {
                for (i in it)
                    extensionDelegate(i)
            }
        }
    }

    fun install(uri:String){
        webExtensionController.install(uri).accept { it-> if (it != null) {
            Toast.makeText(context,it.metaData.name+"安装成功",Toast.LENGTH_LONG).show()
        }
        }
        webExtensionController.promptDelegate=object :WebExtensionController.PromptDelegate{
            override fun onInstallPrompt(extension: WebExtension): GeckoResult<AllowOrDeny>? {
                val dlg = PermissionDialog(context, extension)
                 if (dlg.showDialog() === 1) {
                     extensionDelegate(extension)
                     return GeckoResult.allow()
                } else return GeckoResult.deny()
            }
        }
    }

     fun extensionDelegate(extension: WebExtension){
        extension.tabDelegate=object :WebExtension.TabDelegate{
            override fun onNewTab(
                source: WebExtension,
                createDetails: WebExtension.CreateTabDetails
            ): GeckoResult<GeckoSession>? {
                val session=GeckoSession()
                session.webExtensionController.setTabDelegate(source,object :WebExtension.SessionTabDelegate{
                    override fun onCloseTab(
                        source: WebExtension?,
                        session: GeckoSession
                    ): GeckoResult<AllowOrDeny> {
                        return super.onCloseTab(source, session)
                    }
                })

                newSession(session,context)
                return GeckoResult.fromValue(session)
            }
        }
    }
    fun newSession(session: GeckoSession,activity: Activity) {
        val geckoViewModel= activity?.let { ViewModelProvider(it as ViewModelStoreOwner)[GeckoViewModel::class.java] }!!
        geckoViewModel.changeSearch(session)
        HomeLivedata.getInstance().Value(false)

    }
}