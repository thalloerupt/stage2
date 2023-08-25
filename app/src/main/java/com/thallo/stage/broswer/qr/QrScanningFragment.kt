package com.thallo.stage.broswer.qr

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.RectF
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.thallo.stage.MainActivity
import com.thallo.stage.R
import com.thallo.stage.session.createSession
import com.thallo.stage.utils.Utils.isPortraitMode
import com.thallo.stage.utils.Utils.playVibrate
import com.thallo.stage.utils.Utils.requireColor
import org.mozilla.gecko.util.ThreadUtils.runOnUiThread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalGetImage class QrScanningFragment: Fragment() {

    var width :Float = 0f
    var height :Float = 0f
    private var scaleY :Float = 0f
    private var scaleX :Float = 0f
    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View= ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)

        setContent {

            var x by remember {
                mutableStateOf((this.width/2).toFloat())
            }
            var y by remember {
                mutableStateOf((this.height/2).toFloat())
            }
            var enableTorch by remember {
                mutableStateOf(false)
            }
            var scan by remember {
                mutableStateOf(false)
            }
            val openDialog = remember {
                mutableStateOf(false)
            }
            var result by remember {
                mutableStateOf("")
            }


            val imageAnalysis = ImageAnalysis
                .Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(requireContext())
            ) { it ->
                val mediaImage: Image? = it.image

                if (mediaImage != null) {
                    // 创建InputImage对象
                    val inputImage: InputImage = InputImage.fromMediaImage(
                        mediaImage,
                        it.imageInfo.rotationDegrees
                    )

                    // 使用BarcodeScanner识别二维码
                    val barcodeScanner = BarcodeScanning.getClient();
                    initScale(it.width,it.height)

                    barcodeScanner
                        .process(inputImage)
                        .addOnSuccessListener { barcodes ->
                            if (barcodes.isNotEmpty()){
                                val barcode = barcodes[0]
                                barcode.boundingBox.let {rect ->
                                    if (rect != null) {
                                        x = rect.centerX().toFloat()-rect.width()
                                        y = rect.centerY().toFloat()+rect.height()*3/2

                                    }

                                }


                                if (barcode.format === Barcode.FORMAT_QR_CODE) {
                                    playVibrate(requireContext(),false)
                                    barcodeScanner.close()
                                    it.close()

                                    Handler().postDelayed({
                                        openDialog.value = true
                                        result = barcode.url?.url!!
                                    }, 1000)
                                    /*val intent = Intent(requireContext(), MainActivity::class.java)
                                    if ((getResources().getConfiguration().screenLayout and
                                                Configuration.SCREENLAYOUT_SIZE_MASK) ===
                                        Configuration.SCREENLAYOUT_SIZE_LARGE)
                                    {
                                        createSession(barcode.url?.url,requireActivity())
                                    }else {
                                        intent.data = Uri.parse(barcode.url?.url)
                                        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        startActivity(intent)
                                    }*/

                                }
                            }



                            scan = barcodes.size != 0

                        }
                        .addOnFailureListener { e ->
                            // 处理扫描失败的情况
                            Log.e("QrScanActivity", "Failed to scan barcode", e)
                        }
                        .addOnCompleteListener { task -> it.close()
                        }
                } else {
                    it.close()
                    Log.d("QrScanActivity", "Failed to scan barcode")

                }
            }
            Box(contentAlignment = Alignment.BottomCenter) {
                CameraView(preview = androidx.camera.core.Preview.Builder().build(), imageAnalysis = imageAnalysis, enableTorch = enableTorch)
                Button(modifier = Modifier.fillMaxSize(),
                    onClick = { enableTorch = !enableTorch},
                    colors = ButtonDefaults.buttonColors(
                        Color.Transparent),
                    shape = RoundedCornerShape(0.dp)
                ) {

                }
                AnimatedVisibility(
                    visible = scan,
                    enter = scaleIn(
                        animationSpec = tween(300),
                        initialScale = 0f,
                        transformOrigin = TransformOrigin.Center),
                    exit = scaleOut(
                        animationSpec = tween(300),
                        targetScale = 0f,
                        transformOrigin = TransformOrigin.Center),
                ) {
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        drawCircle(color = R.color.components.requireColor(requireContext()), radius = 48f ,center = Offset(x.dp.toPx(),y.dp.toPx()) )
                        drawCircle(color = R.color.surface.requireColor(requireContext()), radius = 40f ,center = Offset(x.dp.toPx(),y.dp.toPx()) )
                        Log.d("initScale","$x/$y")

                        this@QrScanningFragment.width = this.size.width
                        this@QrScanningFragment.height = this.size.height
                    }
                }
                Text(modifier = Modifier.padding(bottom = 32.dp), text = "轻触屏幕点亮灯光", color = Color.White)
                Dialog(openDialog,result)




            }



        }

    }

    @Composable
    fun CameraView(
        modifier: Modifier = Modifier,
        preview: Preview,
        imageCapture: ImageCapture? = null,
        imageAnalysis: ImageAnalysis? = null,
        cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
        scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
        enableTorch: Boolean = false,
        focusOnTap: Boolean = false
    ) {

        val context = LocalContext.current

        //1
        val previewView = remember { PreviewView(context) }
        val lifecycleOwner = LocalLifecycleOwner.current

        val cameraProvider by produceState<ProcessCameraProvider?>(initialValue = null) {
            value = context.getCameraProvider()
        }

        val camera = remember(cameraProvider) {
            cameraProvider?.let {
                it.unbindAll()
                it.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    *listOfNotNull(preview, imageAnalysis, imageCapture).toTypedArray()
                )

            }
        }


        // 2
        LaunchedEffect(true) {
            preview.setSurfaceProvider(previewView.surfaceProvider)
            previewView.scaleType = scaleType
        }


        LaunchedEffect(camera, enableTorch) {
            // 控制闪光灯
            camera?.let {
                if (it.cameraInfo.hasFlashUnit()) {
                    it.cameraControl.enableTorch(context, enableTorch)
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                cameraProvider?.unbindAll()
            }
        }
        AndroidView(
            { previewView },
            modifier = modifier
                .fillMaxSize()
                .pointerInput(camera, focusOnTap) {
                    if (!focusOnTap) return@pointerInput

                    detectTapGestures {
                        val meteringPointFactory = SurfaceOrientedMeteringPointFactory(
                            size.width.toFloat(),
                            size.height.toFloat()
                        )

                        // 点击屏幕聚焦
                        val meteringAction = FocusMeteringAction
                            .Builder(
                                meteringPointFactory.createPoint(it.x, it.y),
                                FocusMeteringAction.FLAG_AF
                            )
                            .disableAutoCancel()
                            .build()

                        camera?.cameraControl?.startFocusAndMetering(meteringAction)
                    }


                },
        )
    }



    private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
        suspendCoroutine { continuation ->
            ProcessCameraProvider.getInstance(this).also { cameraProvider ->
                cameraProvider.addListener({
                    continuation.resume(cameraProvider.get())




                }, ContextCompat.getMainExecutor(this))
            }
        }

    private suspend fun CameraControl.enableTorch(context: Context, torch: Boolean): Unit =
        suspendCoroutine {
            enableTorch(torch).addListener(
                {},
                ContextCompat.getMainExecutor(context)
            )
        }
    private fun initScale(imageWidth : Int, imageHeight : Int){

            scaleX = width / imageWidth.toFloat()
            scaleY = height / imageHeight.toFloat()
        //Log.d("initScale","$height/$imageHeight")


    }

    @Composable
    fun Dialog(openDialog: MutableState<Boolean>,result :String){
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    openDialog.value = false
                },
                title = {
                    Text(text = "结果")
                },
                text = {
                    Text(text = result)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text("取消")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDialog.value = false
                            val intent = Intent(requireContext(),MainActivity::class.java)

                            if ((getResources().getConfiguration().screenLayout and
                                        Configuration.SCREENLAYOUT_SIZE_MASK) ===
                                Configuration.SCREENLAYOUT_SIZE_LARGE)
                            {
                                createSession(result,requireActivity())
                            }else {
                                intent.data = Uri.parse(result)
                                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                            }
                        }
                    ) {
                        Text("访问")
                    }
                }
            )
        }
    }

}