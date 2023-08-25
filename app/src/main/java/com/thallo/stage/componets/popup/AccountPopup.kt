package com.thallo.stage.componets.popup

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thallo.stage.R
import com.thallo.stage.componets.TabBottomSheetDialog.Companion.TAG
import com.thallo.stage.fxa.AccountManagerCollection
import com.thallo.stage.fxa.AccountProfile
import com.thallo.stage.fxa.AccountProfileViewModel
import com.thallo.stage.fxa.AccountStateViewModel
import com.thallo.stage.utils.Utils.requireColor
import kotlinx.coroutines.launch
import mozilla.components.concept.sync.DeviceCapability
import mozilla.components.concept.sync.DeviceCommandOutgoing
import mozilla.components.concept.sync.Profile
import mozilla.components.service.fxa.manager.FxaAccountManager
import mozilla.components.service.fxa.sync.SyncReason

class AccountPopup : BottomSheetDialogFragment() {
    private lateinit var fxaViewModel :AccountProfileViewModel
    private lateinit var accountManagerCollection: AccountManagerCollection
    private lateinit var accountStateViewModel: AccountStateViewModel
    private lateinit var accountManager: FxaAccountManager



    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        fxaViewModel = ViewModelProvider(requireActivity())[AccountProfileViewModel::class.java]
        accountManagerCollection = ViewModelProvider(requireActivity())[AccountManagerCollection::class.java]
        accountStateViewModel = ViewModelProvider(requireActivity())[AccountStateViewModel::class.java]
        lifecycleScope.launch {
            accountManagerCollection.data.collect(){
                accountManager = it

        }
        }

        setContent {
            MaterialTheme {
                val profile by fxaViewModel.data.collectAsState()
                val panelOption = remember {
                    arrayListOf("立即同步","接受标签","退出账户")
                }
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (avatar,panel,background,lottie) = createRefs()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    GlideImage(
                        model = profile.avatar,
                        contentDescription = "",
                        modifier = Modifier
                            .width(1024.dp)
                            .height(1024.dp)
                            .constrainAs(background) {
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .blur(256.dp)
                    )
                    }

                    avatar(modifier = Modifier.constrainAs(avatar){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        //bottom.linkTo(parent.bottom)
                    } ,profile)
                    operationPanel(modifier = Modifier
                        .height(256.dp)
                        .constrainAs(panel) {
                            width = Dimension.fillToConstraints
                            top.linkTo(avatar.bottom, 32.dp)
                            start.linkTo(parent.start, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                            //bottom.linkTo(parent.bottom,16.dp)
                        },panelOption)

                }




            }
        }
    }
    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun avatar(modifier: Modifier,profile: AccountProfile){


        ConstraintLayout(modifier = modifier) {
            val (avatar,name,email) = createRefs()
            GlideImage(
                model = profile.avatar,
                contentDescription = "",
                modifier = Modifier
                    .width(72.dp)
                    .height(72.dp)
                    .constrainAs(avatar) {
                        top.linkTo(parent.top, 32.dp)
                        start.linkTo(parent.start, 16.dp)

                    }
            ){
                it.circleCrop()
            }


                Text(text =profile.displayName.toString(),
                    modifier = Modifier.constrainAs(name){
                        top.linkTo(avatar.top)
                        start.linkTo(avatar.end, 16.dp)
            }
                    ,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = R.color.components.requireColor(requireContext())
                )
            Text(text =profile.email.toString(),
                modifier = Modifier.constrainAs(email){
                    start.linkTo(avatar.end, 16.dp)
                    bottom.linkTo(avatar.bottom)
                }
                , fontSize = 14.sp,
                color = R.color.components.requireColor(requireContext())
            )




        }

    }
    @Composable
    fun operationPanel(modifier: Modifier,panelOption:ArrayList<String>){
        ConstraintLayout(modifier = modifier) {
            var (background,option) = createRefs()
            Canvas( modifier = Modifier
                .alpha(0.5f)
                .fillMaxSize()
                .constrainAs(background)
                {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, onDraw = {
                val canvasWidth = size.width
                val canvasHeight = size.height
                drawRoundRect(Color(requireActivity().getColor(R.color.onSurface)), size = Size(width = canvasWidth, height = canvasHeight),cornerRadius = CornerRadius(64F,64F))
            })

            LazyColumn(content = {
                items (panelOption.size)
                {
                  Column(modifier = Modifier
                      .fillMaxSize()
                      .clickable {
                          when (it) {
                              0 -> {
                                  lifecycleScope.launch {
                                      accountManager.syncNow(SyncReason.User)
                                      accountManager
                                          .authenticatedAccount()
                                          ?.deviceConstellation()
                                          ?.pollForCommands()

                                  }

                              }

                              1 -> {
                                  ReceivedTabPopup().show(parentFragmentManager,TAG)
                                  dismiss()
                              }
                              2 -> {
                                  lifecycleScope.launch {
                                      accountManager.logout()
                                  }
                                  dismiss()
                              }
                          }
                      }
                      .height(64.dp),
                      verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                      Text(text = panelOption[it],modifier = Modifier, color = R.color.components.requireColor(requireContext()))
                     // Divider()
                  }
                }

            },
                modifier = Modifier
                    .constrainAs(option)
            {
                width = Dimension.fillToConstraints
                top.linkTo(background.top)
                bottom.linkTo(background.bottom)
                start.linkTo(background.start)
                end.linkTo(background.end)
            }
            )






        }
    }
    @Composable
    fun operationItem(modifier: Modifier,text:String){
        Button(onClick = { /*TODO*/ }, modifier = modifier
            .padding(16.dp)
            .height(72.dp), shape = ButtonDefaults.textShape) {
            Text(text = text)
        }

    }


}