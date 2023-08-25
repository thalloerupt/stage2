package com.thallo.stage.componets.popup

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thallo.stage.R
import com.thallo.stage.fxa.AccountManagerCollection
import com.thallo.stage.fxa.TabReceivedViewModel
import com.thallo.stage.session.createSession
import kotlinx.coroutines.launch
import mozilla.components.concept.sync.TabData
import mozilla.components.feature.accounts.push.SendTabFeature
import mozilla.components.service.fxa.manager.FxaAccountManager
import mozilla.components.service.fxa.sync.SyncReason

class ReceivedTabPopup : BottomSheetDialogFragment() {
    private lateinit var accountManagerCollection : AccountManagerCollection
    private lateinit var fxaAccountManager: FxaAccountManager
    private lateinit var receivedTabPopupObervers: ReceivedTabPopupObervers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
        accountManagerCollection = ViewModelProvider(requireActivity())[AccountManagerCollection::class.java]
        receivedTabPopupObervers = ViewModelProvider(requireActivity())[ReceivedTabPopupObervers::class.java]
        receivedTabPopupObervers.changeState(true)
        lifecycleScope.launch {
            accountManagerCollection.data.collect(){
                fxaAccountManager = it

            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)



        lifecycleScope.launch {
            fxaAccountManager.syncNow(SyncReason.User)
            fxaAccountManager
                .authenticatedAccount()
                ?.deviceConstellation()
                ?.pollForCommands()

        }



        setContent {
            MaterialTheme() {
                val tabReceivedViewModel:TabReceivedViewModel = viewModel()
                SendTabFeature(fxaAccountManager) { device, tabs ->
                    // handle tab data here.
                    tabReceivedViewModel.changeTabs(device!!,tabs)
                }
                val tabs = tabReceivedViewModel.tabs.collectAsState()
                val device = tabReceivedViewModel.device.collectAsState()
                ConstraintLayout{
                    val (lottie,panel) = createRefs()
                    Loader(modifier = Modifier
                        .height(128.dp)
                        .width(128.dp)
                        .constrainAs(lottie) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, 24.dp)
                        }
                    )

                    LazyColumn(
                        modifier = Modifier.constrainAs(panel){
                            width = Dimension.fillToConstraints
                            bottom.linkTo(lottie.top,16.dp)
                            start.linkTo(parent.start,16.dp)
                            end.linkTo(parent.end,16.dp)

                        },
                        content = {
                        items(tabs.value.size){
                            Card(modifier = Modifier.clickable {
                                createSession(tabs.value[it].url,requireActivity())
                                dismiss()

                            }) {
                                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                                    val (icon,myDevice,title,url) = createRefs()
                                    Image(
                                        modifier = Modifier.height(24.dp).width(24.dp).constrainAs(icon){
                                            top.linkTo(parent.top,8.dp)
                                            start.linkTo(parent.start,8.dp)
                                        },
                                        painter = painterResource(id = R.drawable.pc_display),
                                        contentDescription = "")
                                    Text(text = device.value.displayName,modifier = Modifier
                                        .basicMarquee()
                                        .constrainAs(myDevice) {
                                            width = Dimension.fillToConstraints
                                            height = Dimension.fillToConstraints
                                            top.linkTo(icon.top)
                                            bottom.linkTo(icon.bottom)
                                            start.linkTo(icon.end,8.dp)
                                            end.linkTo(parent.end,8.dp)

                                        })
                                    Text(text = tabs.value[it].title,
                                        modifier = Modifier
                                            .basicMarquee()
                                            .constrainAs(title) {
                                                width = Dimension.fillToConstraints
                                                height = Dimension.wrapContent
                                                top.linkTo(myDevice.bottom,8.dp)
                                                start.linkTo(parent.start,8.dp)
                                                end.linkTo(parent.end,8.dp)


                                            }
                                        ,
                                        maxLines = 1)
                                    Text(text = tabs.value[it].url,
                                        modifier = Modifier
                                            .basicMarquee()
                                            .constrainAs(url) {
                                                width = Dimension.fillToConstraints
                                                height = Dimension.wrapContent
                                                top.linkTo(title.bottom,8.dp)
                                                start.linkTo(parent.start,8.dp)
                                                end.linkTo(parent.end,8.dp)
                                                bottom.linkTo(parent.bottom,8.dp)
                                            }
                                        ,
                                        maxLines = 1)
                                }

                            }
                        }
                    })


                }

            }
        }

    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        receivedTabPopupObervers.changeState(false)
    }
    @Composable
    fun Loader(modifier: Modifier) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.receive_wait_2))
        val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier
        )
    }
}