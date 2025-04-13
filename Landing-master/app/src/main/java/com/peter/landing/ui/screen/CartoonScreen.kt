package com.peter.landing.ui.screen

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.LandingTopBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.regex.Pattern
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import com.peter.landing.domain.FileService
import com.peter.landing.domain.StableDiffusionService

@Composable
fun CartoonScreen(
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
    onRefresh: () -> Unit,
    generationState: MutableState<GenerationState>
) {
    val context = LocalContext.current
    val imageUrl = remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val generationState = remember { mutableStateOf(GenerationState.Idle) }
    val positivePrompt = remember { mutableStateOf("[4koma] In this emotionally nuanced four-panel comic, we follow the journey of <Anon Chihaya>, a spirited high school girl with flowing pink hair and gentle gray eyes, as she navigates the challenges of studying abroad in the UK.\n" +
            "[SCENE-1] <Anon Chihaya> stands at the bustling international arrivals gate of a London airport, suitcase in hand, beaming with excitement. Her eyes shimmer with hope as she wears her school-issued blazer. Behind her, a “Welcome to the UK” banner flutters above crowds of arriving students and signs in English. A soft golden light filters through the glass ceiling, symbolizing new beginnings.\n" +
            "[SCENE-2] In a brightly lit British classroom filled with chatter, <Anon> stands hesitantly near a group of classmates gathered around a shared project. Her posture is slightly hunched, clutching her English textbook tightly to her chest. The students, speaking rapidly in English with expressive gestures, barely notice her. A speech bubble from <Anon> shows a simple phrase: “C-Can I join...?”—but no one responds, their backs turned.\n" +
            "[SCENE-3] Seated alone in the school cafeteria, <Anon> pokes at a plate of unfamiliar food. The surrounding tables are filled with lively student groups laughing and talking, their voices forming a faded blur behind her. Her expression is a quiet mixture of confusion and longing. A thought bubble shows Japanese script: 「みんなと話せたらいいのに…」(“I wish I could talk with them…”). The background is softly muted, emphasizing her isolation.\n" +
            "[SCENE-4] Later that evening, in her small dorm room, <Anon> sits at her desk under a warm lamp light, a determined spark now in her eyes. An English workbook is open before her, and sticky notes with vocabulary cover the wall. She practices pronunciation with earbuds in, a slight smile forming as she whispers, “I will… try again tomorrow.” A steaming mug beside her reads, “Never give up.”") }
    val negativePrompt = remember { mutableStateOf("lowers, bad anatomy, bad hands, text,error, missing fngers,extra digt ,fewer digits,cropped, wort quality ,low quality,normal quality, jpeg artifacts,signature,watermark, username, blurry, bad feet,") }

    LaunchedEffect(Unit) {
        imageUrl.value = null
    }

    CartoonContent(
        navigateToTerms = navigateToTerms,
        navigateTo = navigateTo,
        imageUrl = imageUrl.value,
        onRefresh = {
            scope.launch {
                imageUrl.value = FileService.getLatestImageUrl(context)
            }
        },
        generateImage = {
            val pos = positivePrompt.value
            val neg = negativePrompt.value
            scope.launch {
                generationState.value = GenerationState.Loading

                StableDiffusionService.sendPromptToComfyUI(pos, neg) { response ->
                    val jsonResponse = JSONObject(response)
                    val newImageUrl = jsonResponse.optString("image_url", null)

                    generationState.value = GenerationState.Generating

                    if (newImageUrl != null) {
                        imageUrl.value = newImageUrl
                    }
                    // 延迟后刷新
                    scope.launch {
                        delay(60000)
                        onRefresh()
                        generationState.value = GenerationState.Idle
                    }
                }
            }
        },
        generationState = generationState
    )
}

@Composable
private fun CartoonContent(
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
    imageUrl: String?,
    onRefresh: () -> Unit,
    generateImage: () -> Unit,
    generationState: MutableState<GenerationState>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LandingTopBar(
                currentDestination = LandingDestination.Main.Cartoon,
                navigateTo = navigateTo,
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                imageUrl?.let {
                    GlideImage(
                        imageUrl = it,
                        modifier = Modifier.fillMaxWidth()
                    )
                } ?: Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            Spacer(modifier = Modifier.height(64.dp))
        }

        // 固定底部操作栏
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (generationState.value == GenerationState.Idle) {
                        onRefresh()
                    }
                },
                enabled = generationState.value == GenerationState.Idle,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text("刷新")
            }

            Button(
                onClick = {
                    if (generationState.value == GenerationState.Idle) {
                        generateImage()
                    }
                },
                enabled = generationState.value == GenerationState.Idle,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                when (generationState.value) {
                    GenerationState.Idle -> Text("点击生成")
                    GenerationState.Loading -> Text("加载中…")
                    GenerationState.Generating -> Text("生成中…")
                }
            }
        }
    }
}

@Composable
fun GlideImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                adjustViewBounds = true
                scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(context)
                    .load(imageUrl)
                    .apply(RequestOptions().placeholder(android.R.drawable.progress_indeterminate_horizontal))
                    .fitCenter()
                    .into(this)
            }
        },
        modifier = modifier
    )
}

enum class GenerationState {
    Idle,         // 空闲
    Loading,      // 请求发送中
    Generating    // 收到响应但图片未刷新
}
