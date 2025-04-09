package com.peter.landing.ui.imageTranslation

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.Executor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImageTranslationScreen(
    navigateTo: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { isGranted ->
            if (!isGranted) {
                Toast.makeText(context, "需要相机权限才能使用此功能", Toast.LENGTH_LONG).show()
            }
        }
    )
    
    var detectedText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }
    
    var previewUseCase by remember { mutableStateOf<Preview?>(null) }
    var imageCaptureUseCase by remember { mutableStateOf<ImageCapture?>(null) }
    
    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Camera Preview
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (cameraPermissionState.status.isGranted) {
                AndroidView(
                    factory = { context ->
                        PreviewView(context).also { previewView ->
                            setupCamera(
                                context,
                                lifecycleOwner,
                                previewView,
                                onPreviewUseCase = { previewUseCase = it },
                                onImageCaptureUseCase = { imageCaptureUseCase = it }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                
                // Capture Button
                FloatingActionButton(
                    onClick = {
                        captureImage(
                            imageCaptureUseCase,
                            context,
                            onDetectedText = { text -> detectedText = text },
                            onTranslatedText = { text -> translatedText = text }
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = "拍照"
                    )
                }
            }
        }
        
        // Results Container
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .weight(0.4f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = detectedText.ifEmpty { "检测到的文本" },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = translatedText.ifEmpty { "翻译结果" },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F5E9))
                    .padding(8.dp)
            )
        }
    }
}

private fun setupCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onPreviewUseCase: (Preview) -> Unit,
    onImageCaptureUseCase: (ImageCapture) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        
        val imageCapture = ImageCapture.Builder().build()
        
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            
            onPreviewUseCase(preview)
            onImageCaptureUseCase(imageCapture)
        } catch (e: Exception) {
            Toast.makeText(context, "相机启动失败", Toast.LENGTH_SHORT).show()
        }
    }, ContextCompat.getMainExecutor(context))
}

private fun captureImage(
    imageCapture: ImageCapture?,
    context: Context,
    onDetectedText: (String) -> Unit,
    onTranslatedText: (String) -> Unit
) {
    val imageCapture1 = imageCapture ?: return
    
    imageCapture1.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                processImage(
                    image,
                    context,
                    onDetectedText,
                    onTranslatedText
                )
            }
            
            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "拍照失败", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

private fun processImage(
    imageProxy: ImageProxy,
    context: Context,
    onDetectedText: (String) -> Unit,
    onTranslatedText: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val detectedText = visionText.text
                onDetectedText(detectedText)
                
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(com.google.mlkit.nl.translate.TranslateLanguage.ENGLISH)
                    .setTargetLanguage(com.google.mlkit.nl.translate.TranslateLanguage.CHINESE)
                    .build()
                val translator = Translation.getClient(options)
                
                translator.downloadModelIfNeeded()
                    .addOnSuccessListener {
                        translator.translate(detectedText)
                            .addOnSuccessListener { translatedText ->
                                onTranslatedText(translatedText)
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "翻译失败", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "翻译模型下载失败", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "文字识别失败", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
} 