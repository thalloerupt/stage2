package com.thallo.stage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class LabHomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)

            setContent {



                var inputtext by rememberSaveable { mutableStateOf("") }

                TransparentSystemBars()
                MaterialTheme() {

                    ConstraintLayout (modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush =
                            ShaderBrush(ImageShader(ImageBitmap.imageResource(id = R.drawable.jpg)))
                        )){
                        val (button,inputView) = createRefs()
                        //OutlinedTextField(value = , onValueChange = )

                        OutlinedTextField(
                            colors = TextFieldDefaults.colors(
                                disabledIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                cursorColor= Color.White,

                            ),
                            shape = RoundedCornerShape(64.dp),
                            value = inputtext,
                            onValueChange = { inputtext = it },
                            modifier = Modifier
                                .constrainAs(inputView) {
                                    height = Dimension.value(56.dp)
                                    width = Dimension.fillToConstraints
                                    start.linkTo(parent.start,32.dp)
                                    end.linkTo(parent.end,32.dp)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }

                        )


                    }


                }
            }
        }
    }


    @Composable
    fun TransparentSystemBars() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = isSystemInDarkTheme()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons,
                isNavigationBarContrastEnforced = false,)
        }
    }


}