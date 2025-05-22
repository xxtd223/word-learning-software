package com.peter.landing.ui.screen

import android.content.Context
import android.util.Log
import android.webkit.WebView
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
import androidx.compose.foundation.background
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.peter.landing.ui.viewModel.DeepSeekViewModel
import com.peter.landing.domain.GlobalTracker


@Composable
fun CartoonScreen(
    viewModel: DeepSeekViewModel,
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
    onRefreshComic: () -> Unit,
    onRefreshStory: () -> Unit,
    generationState: MutableState<GenerationState>
) {
    //val permanentFullResponse by remember { derivedStateOf { viewModel.permanentFullResponse } }
    val globalContent = remember { GlobalTracker.globalContent }

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
        viewModel = viewModel,
        navigateToTerms = navigateToTerms,
        navigateTo = navigateTo,
        imageUrl = imageUrl.value,
        onRefreshComic = {
            scope.launch {
                //clearGlideCache(context)
                imageUrl.value = FileService.getLatestImageUrl(context)
               }
        },
        onRefreshStory = {
            scope.launch {
                //viewModel.clearConversation()
                delay(100) // 确保后台数据更新后再读取
                Log.d("StoryPageDebug", "当前历史: ${globalContent}\n")
                val fePrompt="为了方便我双语学习，请将之前生成的故事整理为一句英文一句中文翻译的形式，并用不同的颜色区分，为每一句标上序号，划出重点词汇。请返回HTML格式的文本，不要回复除了故事本身以外的任何内容！不要回复示例！\n" +
                        "示例：\n" +
                        "<ol style=\"-style-position: inside; padding-left: 0;\">\n" +
                        "  <p>1. \"It’s not just any <strong style=\"background-color: yellow;\">apple</strong>,\" Anon murmured, tracing a finger along its smooth surface.</p>\n" +
                        "</ol>\n" +
                        "<p style=\"color: blue; margin-left: 20px;\">\"这不仅仅是一个普通的<span style=\"text-decoration: underline; font-weight: bold;\">苹果</span>，\" Anon轻声说道，手指在它光滑的表面上划过。</p>\n" +
                        "\n" +
                        "<ol style=\"-style-position: inside; padding-left: 0;\">\n" +
                        "  <p>2. \"Legend says it grants a single, unfiltered <strong style=\"background-color: yellow;\">truth</strong> to whoever takes a bite.\"</p>\n" +
                        "</ol>\n" +
                        "<p style=\"color: blue; margin-left: 20px;\">\"传说说，它会赋予咬下去的人一个<span style=\"text-decoration: underline; font-weight: bold;\">真相</span>，且不加过滤.\"</p>\n" +
                        "\n" +
                        "<ol style=\"-style-position: inside; padding-left: 0;\">\n" +
                        "  <p>3. Jotaro pushed off the wall, his black hair casting a shadow over his sharp <strong style=\"background-color: yellow;\">features</strong>.</p>\n" +
                        "</ol>\n" +
                        "<p style=\"color: blue; margin-left: 20px;\">Jotaro推开墙壁，黑色的头发在他锐利的<span style=\"text-decoration: underline; font-weight: bold;\">面容</span>上投下阴影。</p>\n" +
                        "\n" +
                        "<ol style=\"-style-position: inside; padding-left: 0;\">\n" +
                        "  <p>4. \"And you believe that?\"</p>\n" +
                        "</ol>\n" +
                        "<p style=\"color: blue; margin-left: 20px;\">\"你相信这个吗？\"</p>\n" +
                        "\n" +
                        "<ol style=\"-style-position: inside; padding-left: 0;\">\n" +
                        "  <p>5. Anon smirked. \"Won’t know until I try.\" She lifted the <strong style=\"background-color: yellow;\">apple</strong>, her pulse quickening.</p>\n" +
                        "</ol>\n" +
                        "<p style=\"color: blue; margin-left: 20px;\">Anon露出了笑容。“不试怎么知道。”她举起<span style=\"text-decoration: underline; font-weight: bold;\">苹果</span>，脉搏加速。</p>\n" +
                        "\n" +
                        "<ol style=\"-style-position: inside; padding-left: 0;\">\n" +
                        "  <p>6. Just as her teeth grazed the skin, Jotaro’s hand shot out, gripping her <strong style=\"background-color: yellow;\">wrist</strong>.</p>\n" +
                        "</ol>\n" +
                        "<p style=\"color: blue; margin-left: 20px;\">正当她的牙齿轻轻碰到皮肤时，Jotaro的手迅速伸出，紧紧抓住她的<span style=\"text-decoration: underline; font-weight: bold;\">手腕</span>。</p>\n" +
                        "\n" +
                        "<ol style=\"-style-position: inside; padding-left: 0;\">\n" +
                        "  <p>7. \"Idiot. What if it’s poisoned?\"</p>\n" +
                        "</ol>\n" +
                        "<p style=\"color: blue; margin-left: 20px;\">\"傻瓜。如果它是毒药呢？\"</p>\n" +
                        "\n" +
                        "<ol style=\"-style-position: inside; padding-left: 0;\">\n" +
                        "  <p>8. Their eyes locked—gray meeting <strong style=\"background-color: yellow;\">blue</strong>—a silent battle of <strong style=\"background-color: yellow;\">wills</strong>.</p>\n" +
                        "</ol>\n" +
                        "<p style=\"color: blue; margin-left: 20px;\">他们的目光交汇——灰色与<span style=\"text-decoration: underline; font-weight: bold;\">蓝色</span>相遇——一场无声的<span style=\"text-decoration: underline; font-weight: bold;\">意志</span>较量。</p>\n" +
                        "\n" +
                        "<ol style=\"-style-position: inside; padding-left: 0;\">\n" +
                        "  <p>9. Then, the <strong style=\"background-color: yellow;\">apple</strong> split cleanly in half without being bitten, its core glowing faintly.</p>\n" +
                        "</ol>\n" +
                        "<p style=\"color: blue; margin-left: 20px;\">然后，<span style=\"text-decoration: underline; font-weight: bold;\">苹果</span>没有被咬下，却整齐地裂开成两半，果核微微发光。</p>\n" +
                        "\n" +
                        "<ol style=\"-style-position: inside; padding-left: 0;\">\n" +
                        "  <p>10. A whisper echoed between them: <em>\"The truth you seek is already known.\"</em></p>\n" +
                        "</ol>\n" +
                        "<p style=\"color: blue; margin-left: 20px;\">一声耳语在他们之间回荡：“你寻求的真相早已为人知。”</p>"
                val combinedPrompt = "$globalContent\n${fePrompt}\n$"
                viewModel.sendPrompt(combinedPrompt)
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
                onRefreshComic()
                generationState.value = GenerationState.Idle
            }
        },
        generationState = generationState
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CartoonContent(
    viewModel: DeepSeekViewModel,
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
    imageUrl: String?,
    onRefreshComic: () -> Unit,
    onRefreshStory: () -> Unit,
    generateImage: () -> Unit,
    generationState: MutableState<GenerationState>
) {
    val pagerState = rememberPagerState()
    val titleList = listOf("漫画", "故事")
    val icons = listOf(
        R.drawable.ic_cartoon,
        R.drawable.ic_story
    )
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

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                contentColor = MaterialTheme.colorScheme.tertiary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    )
                },
                modifier = Modifier.height(48.dp)
            ) {
                titleList.forEachIndexed { index, title ->
                    Tab(
                        selected = index == pagerState.currentPage,
                        onClick = {  },
                        modifier = Modifier.height(38.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(icons[index]),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }

            // HorizontalPager切换左右页面（故事/漫画）
            HorizontalPager(
                pageCount = 2,
                state = pagerState,
                pageSpacing = 16.dp,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> {
                        // 漫画界面
                        ComicPage(imageUrl, generationState)
                    }
                    1 -> {
                        // 故事界面
                        StoryPage(viewModel = viewModel)
                    }
                }
            }
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
            ActionButton(
                text = "刷新漫画",
                iconResId = R.drawable.ic_story_clear, //图标
                onClick = {
                    if (generationState.value == GenerationState.Idle) {
                        onRefreshComic()
                        //viewModel.refreshFullResponse()
                    }
                },
                enabled = generationState.value == GenerationState.Idle,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            ActionButton(
                text = "刷新故事",
                iconResId = R.drawable.ic_story_fourthbutton,
                onClick = {
                    if (generationState.value == GenerationState.Idle) {
                        onRefreshStory()
                        //viewModel.refreshFullResponse()
                    }
                },
                enabled = generationState.value == GenerationState.Idle,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun StoryPage(
    viewModel: DeepSeekViewModel // 传入ViewModel
) {

    // 绑定UI状态
    val uiState = viewModel.uiState

    // 筛选仅包含 "助手:" 的回复内容
    val formattedResponse = remember(uiState.fullResponse) {
        uiState.fullResponse
            .split("\n\n\n")
            .filter { it.startsWith("助手:") }
            .joinToString("\n") { it.removePrefix("助手:").trim() } // 去掉 "助手:" 前缀，展示内容
    }

    LaunchedEffect(uiState.fullResponse) {
        Log.d("StoryPageDebug", "当前 fullResponse: ${uiState.fullResponse}")
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // 给内容添加垂直滚动
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                // 使用 WebView 渲染 HTML 格式的文本
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            // 开启 JavaScript 支持
                            settings.javaScriptEnabled = true
                            // 防止 WebView 缩放
                            settings.setSupportZoom(false)
                            settings.builtInZoomControls = false
                            settings.displayZoomControls = false

                            // 加载HTML内容
                            loadDataWithBaseURL(
                                null,
                                formattedResponse.ifBlank { "暂无生成内容。" },
                                "text/html",
                                "UTF-8",
                                null
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

@Composable
fun ComicPage(imageUrl: String?, generationState: MutableState<GenerationState>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            //.padding(12.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // 垂直滚动
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
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            } else {
                GlideImage(
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight() // 保持图片的纵向适配
                )
            }
            Spacer(modifier = Modifier.height(70.dp))
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

@Composable
fun ActionButton(
    text: String,
    iconResId: Int,
    onClick: () -> Unit,
    enabled: Boolean = true,
    loadingText: String? = null,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 1.dp
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = if (isLoading && loadingText != null) loadingText else text,
                fontSize = 14.sp
            )
        }
    }
}
