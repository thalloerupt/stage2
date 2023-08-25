package com.thallo.stage.fxa.sync

import android.accounts.AccountManager
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.thallo.stage.fxa.AccountManagerCollection
import kotlinx.coroutines.launch
import mozilla.components.browser.storage.sync.PlacesHistoryStorage
import mozilla.components.concept.storage.PageVisit
import mozilla.components.concept.storage.VisitType
import mozilla.components.service.fxa.SyncEngine
import mozilla.components.service.fxa.manager.FxaAccountManager
import mozilla.components.service.fxa.sync.GlobalSyncableStoreProvider
import mozilla.components.service.fxa.sync.SyncReason

class HistorySync(val context: Context) {
    private var historyStorage: Lazy<PlacesHistoryStorage> = lazy {
        PlacesHistoryStorage(context)
    }
    private var accountManagerCollection: AccountManagerCollection = ViewModelProvider(context as ViewModelStoreOwner)[AccountManagerCollection::class.java]
    private lateinit var accountManager: FxaAccountManager

    init {
        GlobalSyncableStoreProvider.configureStore(SyncEngine.History to historyStorage)
        (context as LifecycleOwner).lifecycleScope.launch {
            accountManagerCollection.data.collect(){
                accountManager = it


            }

        }
    }

    fun sync(url :String){
        (context as LifecycleOwner).lifecycleScope.launch {
            historyStorage.value.recordVisit(url, PageVisit(VisitType.LINK))
            accountManager.syncNow(SyncReason.User)



        }
    }
}