package com.peter.landing.ui.screen.ds

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.peter.landing.ui.component.MarkdownRenderer
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.viewModel.DeepSeekViewModel
import kotlinx.coroutines.launch
import com.peter.landing.R

var mess by mutableStateOf("")

val description = "## 角色\n" +
        "你是一个专业的四格漫画画家，能够根据用户提供的简单漫画故事和角色设定，丰富每一格漫画的画面描述，并使用英文输出，描述开头需带上[4koma]。\n" +
        "\n" +
        "## 技能\n" +
        "1. 细致描绘每一格漫画中的人物、场景、物品等元素。\n" +
        "2. 运用生动形象的词汇和丰富的想象力，使描述更具吸引力。\n" +
        "3. 按照给定的格式分别描述每一格漫画。\n" +
        "\n" +
        "## 示例\n" +
        "[4koma] In this delightful and charming sequence, [SCENE-1] <Sora>, a creative and observant young artist with blue eyes and short purple hair, is seated at a restaurant table, diligently sketching on a napkin while a waiter, wearing a red and white striped apron, approaches with a concerned expression, requesting more napkins; [SCENE-2] as the scene transitions, <Sora> is now in a car, eyes sparkling with inspiration, pointing excitedly at a small, round creature with a red shell on the dashboard, while musical notes float in the air, indicating a moment of creative epiphany; [SCENE-3] at the beach, <Sora> is engrossed in an intense gaze with a fish-like creature emerging from the water, the sun shining brightly in the background, casting a warm glow over the scene; [SCENE-4] back in the car, <Sora> joyfully exclaims upon finding ink, ready to capture the new inspiration, while a squid-like creature, with a mischievous expression, stands nearby, holding a pen, as if ready to assist in the creative process.\n" +
        "\n" +
        "## 要求\n" +
        "1. 描述要详细、准确，充分展现漫画的情节和氛围。\n" +
        "2. 语言生动、有趣，富有表现力。\n" +
        "3. 严格按照[4koma]和[SCENE-1/2/3/4]的格式进行描述。\n" +
        "4. 输出一定是英文！！！！\n" +
        "\n" +
        "## 限制\n" +
        "1. 描述总长度不超过 350 个单词。\n" +
        "2. 仅围绕用户提供的漫画故事和角色设定进行描述，不添加无关内容。" +
        "3. 角色设定：Anon Chihaya（女，粉色长发，灰色眼睛）,Jotaro Kujo（男，黑色短发，蓝色眼睛） \n" +
        "如果你准备好了，那么故事为："

@Composable
fun DeepSeekChatScreen(
    modifier: Modifier = Modifier,
    viewModel: DeepSeekViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val words = viewModel.spellingList
    val selectedWords = remember { mutableStateOf(emptySet<String>()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()


    // 根据是否有聊天内容决定权重分配
    val hasChatContent = viewModel.uiState.fullResponse.isNotEmpty() ||
            viewModel.uiState.currentResponse.isNotEmpty() ||
            viewModel.uiState.isLoading

    val wordSectionWeight by animateFloatAsState(
        targetValue = if (hasChatContent) 0.5f else 0.8f,
        animationSpec = tween(durationMillis = 300)
    )

    val chatSectionWeight by animateFloatAsState(
        targetValue = if (hasChatContent) 1.3f else 0.5f,
        animationSpec = tween(durationMillis = 300)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 单词选择区域
        // 单词选择区域 - 添加卡片容器
        Surface(
                modifier = Modifier
                        .weight(wordSectionWeight)
                        .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,

                shadowElevation = 4.dp // 修正参数名
        ) {
            Column(
                    modifier = Modifier.padding(16.dp)
            ) {
                // 标题区域增加图标
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                            painter = painterResource(com.peter.landing.R.drawable.ic_select_story_word_24dp),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(27.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                            text = "故事单词选择区",
                            style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.3.sp,
                                    color = MaterialTheme.colorScheme.primary,
                            )
                    )
                }

                //布局单词按钮
                LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(words.chunked(3)) { rowWords ->
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(
                                        8.dp,
                                        Alignment.CenterHorizontally
                                )
                        ) {
                            rowWords.forEach { word ->
                                WordButton(
                                        word = word,
                                        isSelected = word in selectedWords.value,
                                        onClick = {
                                            selectedWords.value = if (word in selectedWords.value) {
                                                selectedWords.value - word
                                            } else {
                                                selectedWords.value + word
                                            }
                                        }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp)) // 添加垂直间隔

        // 结果显示区域
        ChatHistory(
                history = viewModel.uiState.fullResponse,
                currentResponse = viewModel.uiState.currentResponse,
                isLoading = viewModel.uiState.isLoading,
                modifier = Modifier.weight(chatSectionWeight)
        )

        // 底部按钮区域
        Column( // 用Column包裹两个Row（第1处改动）
                modifier = Modifier
                        .padding(vertical = 2.dp)
                        .drawBehind { // 添加虚线绘制（第2处改动）
                            drawRect(
                                    color =Color(0xFF2C7F3E) ,
                                    style = Stroke(
                                            width = 1.dp.toPx(),
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f)
                                    )
                            )
                        }
                        .padding(4.dp) ,// 添加内边距（第3处改动）,
                        verticalArrangement = Arrangement.spacedBy(1.dp) // 修改3：行间距控制
        ) {
            Row(
                    modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 清除选择按钮
                Button(
                        onClick = { selectedWords.value = emptySet() },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(
                                horizontal = 14.dp,  // 横向内边距（原16dp）
                                vertical = 8.dp      // 纵向内边距（原12dp）
                        ),
                        shape = RoundedCornerShape(12.dp), // 新增圆角
                        enabled = selectedWords.value.isNotEmpty() && !viewModel.uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp, // 添加阴影
                                pressedElevation = 1.dp
                        )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {  // 新增Row
                        Icon(
                                painter = painterResource(com.peter.landing.R.drawable.ic_story_clear), // 使用清除图标
                                contentDescription = null,
                                tint=Color.Unspecified,
                                modifier = Modifier.size(27.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("清除选择", fontSize = 14.sp)
                    }
                }

                // 查询按钮
                Button(
                        onClick = {
                            if (selectedWords.value.isNotEmpty()) {
                                val prompt = if (selectedWords.value.size == 1) {
                                    "角色:你是一个专业的故事家，能够根据用户提供的英文单词，丰富情节，并使用英文输出。 \n" +
                                            "\n" +
                                            "要求 \n" +
                                            "1. 描述要详细、准确，充分展现单词意思。 \n" +
                                            "2. 语言生动、有趣，富有表现力。 \n" +
                                            "3. 输出一定是英文！！！！ \n" +
                                            "\n" +
                                            "限制 \n" +
                                            "1. 描述总长度不超过 350 个单词。 \n" +
                                            "2. 主角（两个人）姓名：Anon Chihaya（女）,Jotaro Kujo（男）\n" +
                                            "3.不添加无关内容。你只需要回答故事就行了，其他的东西一个字也不要说\n " +
                                            "如果你准备好了，请回答：\n" +
                                            "${selectedWords.value.first()}"
                                } else {
                                    "角色:你是一个专业的故事家，能够根据用户提供的英文单词，丰富情节，并使用英文输出。 \n" +
                                            "\n" +
                                            "要求 \n" +
                                            "1. 描述要详细、准确，充分展现单词意思。 \n" +
                                            "2. 语言生动、有趣，富有表现力。 \n" +
                                            "3. 输出一定是英文！！！！ \n" +
                                            "\n" +
                                            "限制 \n" +
                                            "1. 描述总长度不超过 350 个单词。 \n" +
                                            "2. 主角（两个人）姓名：Anon Chihaya（女，粉色长发，灰色眼睛）,Jotaro Kujo（男，黑色短发，蓝色眼睛）\n" +
                                            "3.不添加无关内容。你只需要回答故事就行了，其他的东西一个字也不要说\n " +
                                            "如果你准备好了，请回答" +
                                            "${selectedWords.value.joinToString("、")}"

                                }
                                viewModel.sendPrompt(prompt)

                                selectedWords.value = emptySet() // 查询后清空选择
                            }
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(
                                horizontal = 14.dp,  // 横向内边距（原16dp）
                                vertical = 8.dp      // 纵向内边距（原12dp）
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 1.dp
                        ),
                        enabled = selectedWords.value.isNotEmpty() && !viewModel.uiState.isLoading
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {  // 新增Row
                        Text("生成故事", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.width(4.dp))
                        Icon(
                                painter = painterResource(com.peter.landing.R.drawable.ic_story_send), // 使用搜索图标
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
            Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                        onClick = {
                            val combinedPrompt = "$description\n\n$mess"
                            viewModel.sendSilentPrompt(combinedPrompt)
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(
                                horizontal = 14.dp,  // 横向内边距（原16dp）
                                vertical = 8.dp      // 纵向内边距（原12dp）
                        ),
                        shape = RoundedCornerShape(12.dp), // 新增圆角
                        colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp, // 添加阴影
                                pressedElevation = 1.dp
                        ),
                        enabled = (!viewModel.uiState.isLoading) && (mess.isNotEmpty()) && (!viewModel.hiddenUiState.isLoading)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {  // 新增Row
                        Icon(
                                painter = painterResource(com.peter.landing.R.drawable.ic_story_analyse), // 使用分析图标
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("分析故事结构", fontSize = 14.sp)
                    }
                }
                Button(
                    onClick = {
                        isLoading = true
                        viewModel.sendR()

                        coroutineScope.launch {
                            kotlinx.coroutines.delay(4000)
                            isLoading = false
                            navHostController.navigate(LandingDestination.Main.Cartoon.route) // 跳转到CartoonScreen
                        }
                    },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(
                                horizontal = 14.dp,  // 横向内边距（原16dp）
                                vertical = 8.dp      // 纵向内边距（原12dp）
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 1.dp
                        ),
                        enabled = viewModel.sendFlag
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {  // 新增Row
                        Text(
                            text = if (isLoading) "生成中…" else "生成漫画",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                                painter = painterResource(com.peter.landing.R.drawable.ic_story_fourthbutton), // 使用发送图标
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@Composable
fun WordButton(
        word: String,
        isSelected: Boolean,
        onClick: () -> Unit
) {
    Surface(
            modifier = Modifier
                    .height(48.dp)
                    .padding(4.dp),
            shape = RoundedCornerShape(20.dp),  // 1. 改为圆形胶囊形状
            color = if (isSelected) Color(0xFF4CAF50) else Color(0xFFE8F5E9), // 2. 修改选中颜色
            border = BorderStroke(1.dp, Color(0xFF81C784)), // 3. 添加浅绿色边框
            onClick = onClick
    ) {
        Text(
                text = word,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = if (isSelected) Color.White else Color(0xFF2E7D32), // 4. 调整文字颜色
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium // 增加字重

        )
    }
}


@Composable
fun ChatHistory(
    history: String,
    currentResponse: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(1.dp, Color(0xFF81C784)), // 3. 添加浅绿色边框
            shadowElevation = 8.dp // 2. 增加阴影层级
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val messages = history.split("\n\n\n").filter { it.isNotBlank() }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                reverseLayout = true
            ) {
                // 显示 loading 时的占位消息
                if (isLoading) {
                    item {
                        ChatBubble(
                            text = "思考中",
                            isUser = false,
                            isTyping = true // 动画效果
                        )
                    }
                }

                // 显示完整历史
                items(messages.reversed()) { message ->
                    if (message.startsWith("助手: ")) {
                        val cleanedMessage = message
                            .removePrefix("助手: ")
                            .replace(Regex("<think>.*?</think>", RegexOption.DOT_MATCHES_ALL), "")

                        mess = cleanedMessage

                        ChatBubble(
                            text = cleanedMessage,
                            isUser = false
                        )
                    }
                }
            }

            // 如果完全没有历史和当前响应
            if (history.isEmpty() && currentResponse.isEmpty() && !isLoading) {
                Text(
                    text = "查询结果将显示在这里...",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ChatBubble(
    text: String,
    isUser: Boolean,
    isTyping: Boolean = false,
    modifier: Modifier = Modifier
) {
    val bubbleColor = if (isUser) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f) // 4. 添加透明度
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f) // 4. 添加透明度
    }

    val textColor = if (isUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSecondary
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(
            topStart = if (isUser) 16.dp else 4.dp,
            topEnd = if (isUser) 4.dp else 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
        ),
        color = bubbleColor,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Icon(
                    painter = painterResource(if (isUser) R.drawable.ic_story_bot_24dp else R.drawable.ic_story_user_24dp),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (isUser) "你" else "DeepSeek",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.3.sp
                    ),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                )
            }

            if (isUser || isTyping) {
                Text(
                    text = if (isTyping) "$text..." else text,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 18.sp
                    )
                )
            } else {
                // 使用 Markwon 渲染 Markdown
                MarkdownRenderer(
                    markdown = text,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
