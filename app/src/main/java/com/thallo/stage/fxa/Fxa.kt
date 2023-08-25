package com.thallo.stage.fxa

import android.content.Context
import android.provider.Settings
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.thallo.stage.componets.popup.ReceivedTabPopupObervers
import kotlinx.coroutines.launch
import mozilla.components.browser.storage.sync.PlacesBookmarksStorage
import mozilla.components.concept.sync.*
import mozilla.components.lib.fetch.httpurlconnection.HttpURLConnectionClient
import mozilla.components.service.fxa.*
import mozilla.components.service.fxa.manager.FxaAccountManager
import mozilla.components.service.fxa.sync.GlobalSyncableStoreProvider
import mozilla.components.service.fxa.sync.SyncReason
import mozilla.components.service.fxa.sync.SyncStatusObserver
import mozilla.components.support.rusthttp.RustHttpConfig
import mozilla.components.support.rustlog.RustLog


class Fxa {
    private lateinit var accountManagerCollection: AccountManagerCollection
    private lateinit var accountStateViewModel: AccountStateViewModel
    private lateinit var syncDevicesObserver: SyncDevicesObserver
    private lateinit var fxaViewModel :AccountProfileViewModel
    private lateinit var context: Context
    private var syncState = SyncState()
    private lateinit var mAccountManager: FxaAccountManager

    companion object {
        const val CLIENT_ID = "3c49430b43dfba77"
        const val REDIRECT_URL = "https://accounts.firefox.com/oauth/success/$CLIENT_ID"
    }
    var isLogin:Boolean=false
    private var receivingState = false

    fun init(context: Context):FxaAccountManager{
        this.context = context
        val mContext = context as LifecycleOwner

        RustLog.enable()
        RustHttpConfig.setClient(lazy { HttpURLConnectionClient() })

        fxaViewModel = ViewModelProvider(context as ViewModelStoreOwner)[AccountProfileViewModel::class.java]
        accountManagerCollection = ViewModelProvider(context as ViewModelStoreOwner)[AccountManagerCollection::class.java]
        accountStateViewModel = ViewModelProvider(context as ViewModelStoreOwner)[AccountStateViewModel::class.java]
        syncDevicesObserver = ViewModelProvider(context as ViewModelStoreOwner)[SyncDevicesObserver::class.java]
        val receivedTabPopupObervers = ViewModelProvider(context as ViewModelStoreOwner)[ReceivedTabPopupObervers::class.java]
        var deviceName = Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME)

        val accountManager by lazy {
            FxaAccountManager(
                context,
                ServerConfig(Server.RELEASE, CLIENT_ID, REDIRECT_URL),
                DeviceConfig(
                    name = "Stage on $deviceName",
                    type = DeviceType.MOBILE,
                    capabilities = setOf(DeviceCapability.SEND_TAB),
                    secureStateAtRest = false,
                ),
                SyncConfig(
                    setOf(
                        SyncEngine.Bookmarks,
                        SyncEngine.History

                    ),
                    periodicSyncConfig = PeriodicSyncConfig(periodMinutes = 0, initialDelayMinutes = 1),
                ),
            )
        }

        mAccountManager = accountManager


        mContext.lifecycleScope.launch{
            initState()
        }

        mContext.lifecycleScope.launch {
            receivedTabPopupObervers.state.collect(){
                receivingState = it
            }
        }


        val bookmarksStorage = lazy {
            PlacesBookmarksStorage(context)
        }
        GlobalSyncableStoreProvider.configureStore(SyncEngine.Bookmarks to bookmarksStorage)


        accountManager.register(accountObserver, owner = mContext, autoPause = true)
        // Observe sync state changes.
        accountManager.registerForSyncEvents(syncObserver, owner = mContext, autoPause = true)
        // Observe incoming device commands.
        accountManager.registerForAccountEvents(accountEventsObserver, owner = mContext, autoPause = false)


        mContext.lifecycleScope.launch {
            // Now that our account state observer is registered, we can kick off the account manager.
            accountManager.start()
        }

        accountManagerCollection.change(accountManager)



        return accountManager


    }


    private val deviceConstellationObserver = object : DeviceConstellationObserver {
        override fun onDevicesUpdate(constellation: ConstellationState) {
            syncDevicesObserver.sendAccountState(constellation.otherDevices)
        }
    }

    private val accountObserver = object : AccountObserver {
        lateinit var lastAuthType: AuthType
        override fun onLoggedOut() {
            super.onLoggedOut()
            isLogin = false
            fxaViewModel.changeProfile(
                AccountProfile()
            )
        }

        override fun onAuthenticated(account: OAuthAccount, authType: AuthType) {
            isLogin = true
            (context as LifecycleOwner).lifecycleScope.launch {
                account.deviceConstellation().registerDeviceObserver(
                    deviceConstellationObserver,
                    context as LifecycleOwner,
                    true,
                )
            }

            (context as LifecycleOwner).lifecycleScope.launch {
                mAccountManager
                    .authenticatedAccount()
                    ?.deviceConstellation()
                    ?.refreshDevices()
            }
        }
        override fun onProfileUpdated(profile: Profile) {
            fxaViewModel.changeProfile(
                AccountProfile(profile.uid,
                    profile.email,
                    profile.avatar?.url,
                    profile.displayName
                )
            )

        }
        override fun onFlowError(error: AuthFlowError) {


        }
    }
    private val syncObserver = object : SyncStatusObserver {
        override fun onError(error: Exception?) {
            syncState.copy(error = true,idle = false,start = true)
            accountStateViewModel.sendAccountState(syncState)

        }

        override fun onIdle() {
            syncState.copy(error = false,idle = true,start = true)
            accountStateViewModel.sendAccountState(syncState)
            if (receivingState){
                (context as LifecycleOwner).lifecycleScope.launch {
                    mAccountManager.syncNow(SyncReason.User)
                    mAccountManager
                        .authenticatedAccount()
                        ?.deviceConstellation()
                        ?.pollForCommands()

                }
            }

        }

        override fun onStarted() {
            syncState.copy(error = false,idle = false,start = true)
            accountStateViewModel.sendAccountState(syncState)
        }
    }
    @Suppress("SetTextI18n", "NestedBlockDepth")
    private val accountEventsObserver = object : AccountEventsObserver {
        override fun onEvents(events: List<AccountEvent>) {
            events.forEach {
                when (it) {
                    is AccountEvent.DeviceCommandIncoming -> {
                        when (it.command) {
                            is DeviceCommandIncoming.TabReceived -> {
                                val cmd = it.command as DeviceCommandIncoming.TabReceived
                                var tabsStringified = "Tab(s) from: ${cmd.from?.displayName}\n"
                                cmd.entries.forEach { tab ->
                                    tabsStringified += "${tab.title}: ${tab.url}\n"
                                }
                                android.util.Log.d("tabsStringified","get:$tabsStringified")
                            }
                        }
                    }
                    is AccountEvent.ProfileUpdated -> {

                    }
                    is AccountEvent.AccountAuthStateChanged -> {
                    }
                    is AccountEvent.AccountDestroyed -> {
                    }
                    is AccountEvent.DeviceConnected -> {
                    }
                    is AccountEvent.DeviceDisconnected -> {

                    }
                    is AccountEvent.Unknown ->{

                    }
                }
            }
        }
    }


    suspend fun initState(){
        accountStateViewModel.accountStateFlow.collect(){
            syncState = it
        }
    }



}