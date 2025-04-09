package com.peter.landing.ui.imageTranslation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.peter.landing.databinding.LayoutImageTranslationBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageTranslationFragment : Fragment() {
    private var _binding: LayoutImageTranslationBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutImageTranslationBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        
        binding.captureButton.setOnClickListener {
            takePhoto()
        }
        
        cameraExecutor = Executors.newSingleThreadExecutor()
    }
    
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            
            imageCapture = ImageCapture.Builder().build()
            
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "相机启动失败", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }
    
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    processImage(image)
                }
                
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), "拍照失败", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    
    private fun processImage(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            
            // 创建文本识别器（使用拉丁文识别器，适用于英文）
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val detectedText = visionText.text
                    binding.detectedText.text = detectedText
                    
                    // 创建翻译器
                    val options = TranslatorOptions.Builder()
                        .setSourceLanguage(com.google.mlkit.nl.translate.TranslateLanguage.ENGLISH)
                        .setTargetLanguage(com.google.mlkit.nl.translate.TranslateLanguage.CHINESE)
                        .build()
                    val translator = Translation.getClient(options)
                    
                    // 下载翻译模型（如果需要）
                    translator.downloadModelIfNeeded()
                        .addOnSuccessListener {
                            // 开始翻译
                            translator.translate(detectedText)
                                .addOnSuccessListener { translatedText ->
                                    binding.translatedText.text = translatedText
                                }
                                .addOnFailureListener {
                                    Toast.makeText(requireContext(), "翻译失败", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "翻译模型下载失败", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "文字识别失败", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
    
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "需要相机权限", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }
    
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
} 