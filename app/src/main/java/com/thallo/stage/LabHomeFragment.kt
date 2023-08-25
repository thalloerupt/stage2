package com.thallo.stage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
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
                TransparentSystemBars()
                MaterialTheme() {
                    ConstraintLayout {
                        val (button,geckoView) = createRefs()
                        GeckoView(modifier = Modifier.constrainAs(geckoView) {
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                            top.linkTo(parent.top,0.dp)
                            bottom.linkTo(parent.bottom, 0.dp)
                            start.linkTo(parent.start,0.dp)
                            end.linkTo(parent.end, 0.dp)
                        })

                        /**Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .width(64.dp)
                            .height(64.dp)
                            .alpha(0.5f)
                            .blur(
                                radiusX = 2.dp,
                                radiusY = 2.dp,
                                edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(0.dp))
                            )
                            .constrainAs(button) {
                                bottom.linkTo(parent.bottom, 0.dp)
                                end.linkTo(parent.end, 0.dp)
                            }, colors = ButtonDefaults.buttonColors(Color.White)
                        ) {

                        }**/

                    }


                }
            }
        }
    }
    @Composable
    fun GeckoView(modifier: Modifier){
        AndroidView(
            modifier = modifier, // Occupy the max size in the Compose UI tree
            factory = { context ->
                // Creates custom view
                GeckoView(context).apply {
                    // Sets up listeners for View -> Compose communication
                    this.releaseSession()
                    val session=GeckoSession()
                    session.open(GeckoRuntime.getDefault(this@LabHomeFragment.requireContext()))
                    session.loadUri("https://inftab.com/")
                    this.setSession(session)

                }
            },
            update = { view ->
                // View's been inflated or state read in this block has been updated
                // Add logic here if necessary
                // As selectedItem is read here, AndroidView will recompose
                // whenever the state changes
                // Example of Compose -> View communication
                // view.coordinator.selectedItem = selectedItem.value

            }
        )
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