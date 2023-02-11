package com.thallo.stage.fxa

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mozilla.components.browser.storage.sync.PlacesBookmarksStorage
import mozilla.components.concept.storage.BookmarkNode
import mozilla.components.concept.sync.*
import mozilla.components.lib.fetch.httpurlconnection.HttpURLConnectionClient
import mozilla.components.service.fxa.*
import mozilla.components.service.fxa.R
import mozilla.components.service.fxa.manager.FxaAccountManager
import mozilla.components.service.fxa.sync.GlobalSyncableStoreProvider
import mozilla.components.service.fxa.sync.SyncStatusObserver
import mozilla.components.support.rusthttp.RustHttpConfig
import java.lang.Exception

class Fxa {
    companion object {
        const val CLIENT_ID = "3c49430b43dfba77"
        const val REDIRECT_URL = "https://accounts.firefox.com/oauth/success/$CLIENT_ID"
    }
    lateinit var  profileUpdated:Fxa.ProfileUpdated
    fun init(context: Context):FxaAccountManager{
        context as LifecycleOwner
        RustHttpConfig.setClient(lazy { HttpURLConnectionClient() })
        val bookmarksStorage = lazy {
            PlacesBookmarksStorage(context)
        }
        GlobalSyncableStoreProvider.configureStore(SyncEngine.Bookmarks to bookmarksStorage)
        val accountManager by lazy {
            FxaAccountManager(
                context,
                ServerConfig(Server.RELEASE, CLIENT_ID, REDIRECT_URL),
                DeviceConfig(
                    name = "Stage",
                    type = DeviceType.MOBILE,
                    capabilities = setOf(DeviceCapability.SEND_TAB),
                    secureStateAtRest = true,
                ),
                SyncConfig(
                    setOf(
                        SyncEngine.Bookmarks
                    ),
                    periodicSyncConfig = PeriodicSyncConfig(periodMinutes = 15, initialDelayMinutes = 5),
                ),
            )
        }
        val accountObserver = object : AccountObserver {
            lateinit var lastAuthType: AuthType

            override fun onAuthenticated(account: OAuthAccount, authType: AuthType) {

            }
            override fun onProfileUpdated(profile: Profile) {
                context.lifecycleScope.launch {

                    profileUpdated.onProfileUpdated(profile)

                }

            }
            override fun onFlowError(error: AuthFlowError) {


            }
        }
        val syncObserver = object : SyncStatusObserver {
            override fun onError(error: Exception?) {
            }

            override fun onIdle() {

            }

            override fun onStarted() {
            }
        }
        val accountEventsObserver = object : AccountEventsObserver {
            override fun onEvents(events: List<AccountEvent>) {

            }
        }
        val deviceConstellationObserver = object : DeviceConstellationObserver {
            override fun onDevicesUpdate(constellation: ConstellationState) {

            }
        }
        accountManager.register(accountObserver, owner = context, autoPause = true)
        // Observe sync state changes.
        accountManager.registerForSyncEvents(syncObserver, owner = context, autoPause = true)
        // Observe incoming device commands.
        accountManager.registerForAccountEvents(accountEventsObserver, owner = context, autoPause = true)

        context.lifecycleScope.launch {
            // Now that our account state observer is registered, we can kick off the account manager.
            accountManager.start()
        }
        return accountManager

    }
    interface ProfileUpdated{
        fun onProfileUpdated(profile: Profile)
    }
}