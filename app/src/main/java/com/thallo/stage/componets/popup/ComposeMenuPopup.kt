package com.thallo.stage.componets.popup

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thallo.stage.R
import com.thallo.stage.fxa.AccountManagerCollection
import com.thallo.stage.fxa.SyncDevicesObserver
import com.thallo.stage.session.DelegateLivedata
import com.thallo.stage.session.SessionDelegate
import com.thallo.stage.session.SessionViewModel
import kotlinx.coroutines.launch
import mozilla.components.concept.sync.Device
import mozilla.components.concept.sync.DeviceCommandOutgoing
import mozilla.components.concept.sync.DeviceType
import mozilla.components.service.fxa.manager.FxaAccountManager

class ComposeMenuPopup :BottomSheetDialogFragment(){
    private lateinit var syncDevicesObserver: SyncDevicesObserver
    private lateinit var accountManagerCollection: AccountManagerCollection
    private lateinit var accountManager: FxaAccountManager
    private val sendDevices = arrayListOf<Device>()
    private lateinit var sessionDelegate: SessionDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        syncDevicesObserver = ViewModelProvider(requireActivity())[SyncDevicesObserver::class.java]
        accountManagerCollection = ViewModelProvider(requireActivity())[AccountManagerCollection::class.java]
        lifecycleScope.launch {
            accountManagerCollection.data.collect(){
                accountManager = it

            }
        }


    }
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        DelegateLivedata.getInstance().observe(viewLifecycleOwner){
            sessionDelegate = it
        }
        setContent {
            MyTheme() {
                val deviceList = syncDevicesObserver.syncDevicesStateFlow.collectAsState()
                var moveToRight by remember { mutableStateOf(false) }
                val targetValue = if(moveToRight) 200.dp else 0.dp
                val animation1 = tween<Dp>(durationMillis = 500)
                val startPadding1 by animateDpAsState(targetValue, animationSpec = repeatable(1, animation1),
                    label = ""
                )
                ConstraintLayout {

                    val (devices,pushButton,tip,title,url) = createRefs()
                    Text(
                        text = "向其他设备推送标签",
                        modifier = Modifier.constrainAs(tip) {
                        top.linkTo(parent.top,8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        },
                        fontSize = 16.sp

                    )
                    FilledIconButton(
                        onClick ={
                            moveToRight = true
                            lifecycleScope.launch {
                                accountManager.authenticatedAccount()?.deviceConstellation()?.let {constellation ->
                                    sendDevices?.forEach {
                                        moveToRight = !constellation.sendCommandToDevice(
                                            it.id,
                                            DeviceCommandOutgoing.SendTab(sessionDelegate.mTitle, sessionDelegate.u),
                                        )
                                        sendDevices.remove(it)

                                    }

                                }
                            }

                                 },
                        modifier = Modifier
                            .size(128.dp)
                            .constrainAs(pushButton) {
                                top.linkTo(tip.bottom, 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                    ) {
                        Crossfade(!moveToRight, animationSpec = tween(500), label = "") {
                            // 使用状态进行判断
                            if(it){
                                Icon(painter = painterResource(id = R.drawable.send_fill), contentDescription ="send", modifier = Modifier.size(64.dp).padding(start = startPadding1,bottom =startPadding1 ))

                            }else{
                                Icon(painter = painterResource(id = R.drawable.check_circle_fill), contentDescription ="send", modifier = Modifier.size(64.dp))

                            }
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.constrainAs(devices){
                            width = Dimension.wrapContent
                            height = Dimension.wrapContent
                            top.linkTo(pushButton.bottom,16.dp)
                            start.linkTo(parent.start,8.dp)
                            end.linkTo(parent.end,8.dp)
                            bottom.linkTo(parent.bottom,16.dp)
                        },
                        content = {
                        items(deviceList.value.size){
                            device(deviceList.value[it])
                        }
                    })
                }




            }
        }


    }
    @ExperimentalAnimationApi
    @Composable
    fun device(device: Device) {
        var shown by remember { mutableStateOf(false) }

        ConstraintLayout(modifier = Modifier.padding(8.dp)){
            val (myDevice,checked,name) = createRefs()
            avatar(modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .constrainAs(myDevice) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    top.linkTo(parent.top, 4.dp)
                }
                .clickable {
                    shown = !shown
                    if (shown)
                        sendDevices.add(device)
                    else
                        sendDevices.remove(device)
                           },
                int = getDeviceTypeAvatar(device.deviceType)
            )
            AnimatedVisibility(
                visible = shown,
                // 进入动画设置 fadeIn ，动画规格设置 tween 时长 1000ms，初始透明度 0.3f
                enter = scaleIn(
                    animationSpec = tween(300),
                    initialScale = 0f,
                    transformOrigin = TransformOrigin.Center),
                exit = scaleOut(
                    animationSpec = tween(300),
                    targetScale = 0f,
                    transformOrigin = TransformOrigin.Center),
                modifier = Modifier.constrainAs(checked){
                    end.linkTo(myDevice.end)
                    bottom.linkTo(myDevice.bottom)
                }

            ) {
                checked()
            }

            Text(
                text = device.displayName,
                modifier = Modifier.constrainAs(name){
                    top.linkTo(myDevice.bottom,4.dp)
                    start.linkTo(parent.start,8.dp)
                    end.linkTo(parent.end,8.dp)
                    bottom.linkTo(parent.bottom,4.dp)
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,

            )

        }
    }
    @Composable
    fun avatar(modifier: Modifier,int: Int){
        Box(modifier = modifier.size(64.dp),contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(64.dp)) {
                drawCircle(requireColor(R.color.onSurface))

            }
            Image(painter = painterResource(id = int), contentDescription = "", modifier = Modifier.size(28.dp))

        }
    }

    @Composable
    fun checked (){
        Box(modifier = Modifier.size(20.dp),contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(20.dp)) {
                    drawCircle(Color.White)
            }
            Image(painter = painterResource(id = R.drawable.check_circle_fill), contentDescription = "", modifier = Modifier.size(16.dp))

        }
    }


    private fun requireColor(int: Int) :Color = Color(requireContext().getColor(int))
    private fun getDeviceTypeAvatar(deviceType: DeviceType):Int{
        DeviceType.MOBILE
        return when(deviceType){
            DeviceType.MOBILE ->R.drawable.phone_fill
            DeviceType.DESKTOP ->R.drawable.pc_display

            else -> R.drawable.phone_fill
        }

    }
    @Composable
    fun MyTheme (
        dark: Boolean = isSystemInDarkTheme (),
        dynamic: Boolean = Build. VERSION.SDK_INT >= Build.VERSION_CODES.S,
        content: @Composable () -> Unit
    ) {
        // ColorScheme 配置以及 MaterialTheme
        val colorScheme = if (dynamic) {
            val context = LocalContext.current
            if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            if (dark) dynamicDarkColorScheme(requireContext()) else dynamicLightColorScheme(requireContext())
        }
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }


}