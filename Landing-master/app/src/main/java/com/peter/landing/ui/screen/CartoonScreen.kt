package com.peter.landing.ui.screen

import android.content.Context
import android.widget.ImageView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.LandingTopBar
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.peter.landing.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
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
    val positivePrompt = remember { mutableStateOf("[4koma] In this lively and musical encounter:\n" +
            "[SCENE-1] <Anon Chihaya> sits cross-legged on a grassy patch beneath a cherry blossom tree, a sleek acoustic guitar resting on her lap. Her pink hair sways slightly in the breeze as her fingers strum the strings with joyful precision. Notes float through the air around her, and her gray eyes sparkle with passion. A dreamy smile is on her face as she hums along, clearly lost in her own musical world.\n" +
            "[SCENE-2] A sudden rhythm interrupts the melody—<Jotaro Kujo> appears on the next panel, sitting behind a compact drum set set up in an alleyway. His eyes are focused, blue and intense, as his sticks move with calm precision. The background is a blur of motion lines emphasizing his steady beat. A few curious cats gather around, nodding to the rhythm. Jotaro, unaware of any audience, is entirely in sync with the drums.\n" +
            "[SCENE-3] <Anon> peeks around the corner with wide eyes and a huge grin, guitar slung over her shoulder. She rushes toward Jotaro, her hands animated in excitement as cherry petals float around her. “You play drums?! That’s amazing!” she exclaims, practically bouncing. Jotaro looks up, slightly startled but composed, raising an eyebrow in mild curiosity.\n" +
            "[SCENE-4] The two now face each other—Anon striking a dynamic pose with one hand pointing at Jotaro, the other gripping her guitar, while Jotaro rests his drumsticks on his shoulders with a calm smile. A large, vibrant speech bubble hovers between them: “Let’s start a band!” Musical notes and colorful spark lines burst from the panel, capturing the moment of this new, spontaneous partnership.") }
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
                //clearGlideCache(context)
                imageUrl.value = FileService.getLatestImageUrl(context)
            }
        },
        generateImage = {
            val pos = positivePrompt.value
            val neg = negativePrompt.value
            scope.launch {
                generationState.value = GenerationState.Loading

                withContext(Dispatchers.IO) {
                    StableDiffusionService.sendPromptToComfyUI(pos, neg) { response ->
                        val jsonResponse = JSONObject(response)
                        val newImageUrl = jsonResponse.optString("image_url", null)

                        generationState.value = GenerationState.Generating

                        imageUrl.value = newImageUrl
                    }
                }

                // 延迟后刷新
                delay(60000)
                onRefresh()
                generationState.value = GenerationState.Idle
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
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                if (imageUrl.isNullOrEmpty()) {
                    // imageUrl为空时显示默认占位符图片
                    AndroidView(
                        factory = { context ->
                            ImageView(context).apply {
                                adjustViewBounds = true
                                scaleType = ImageView.ScaleType.FIT_CENTER
                                Glide.with(context)
                                    .load(R.drawable.placeholder_image)
                                    .into(this)
                            }
                        },
                        modifier = Modifier
                            .width(1448.dp)
                            .wrapContentHeight()
                    )
                } else {
                    GlideImage(
                        imageUrl = imageUrl,
                        modifier = Modifier
                            .width(1448.dp)
                            .wrapContentHeight()
                    )
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
                    .apply(RequestOptions()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image) //加载失败时显示默认图片
                    )
                    .fitCenter()
                    .override(1448, 1448)
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

// 清除缓存
@OptIn(DelicateCoroutinesApi::class)
fun clearGlideCache(context: Context) {
    Glide.get(context).clearMemory()
    GlobalScope.launch(Dispatchers.IO) {
        Glide.get(context).clearDiskCache()
    }
}
