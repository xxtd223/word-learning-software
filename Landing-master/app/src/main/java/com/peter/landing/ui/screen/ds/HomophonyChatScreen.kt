package com.peter.landing.ui.screen.ds

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peter.landing.R
import com.peter.landing.ui.viewModel.DeepSeekViewModel



@Composable
fun HomophonyChatScreen(
    modifier: Modifier = Modifier,
    viewModel: DeepSeekViewModel = hiltViewModel()
) {
    val words = viewModel.spellingList
    val selectedWords = remember { mutableStateOf(emptySet<String>()) }

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
                            painter = painterResource(R.drawable.ic_planet),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(27.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                            text = "请选择单词查询谐音梗:",
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
                                    color = Color(0xFF2C7F3E),
                                    style = Stroke(
                                            width = 1.dp.toPx(),
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f)
                                    )
                            )
                        }
                        .padding(4.dp),// 添加内边距（第3处改动）,
                verticalArrangement = Arrangement.spacedBy(1.dp) // 修改3：行间距控制
        ) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
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
                                    "角色:你是一个幽默的语言大师，能够根据用户提供的英文单词，输出谐音梗，中英结合 \n" +
                                            "\n" +
                                            "要求 \n" +
                                            "1. 描述要详细、准确，充分展现单词意思。 \n" +
                                            "2. 语言生动、有趣，富有表现力。 \n" +
                                            "3. 输出中英文结合！ \n" +
                                            "\n" +
                                            "限制 \n" +
                                            "1.不添加无关内容。你只需要回答谐音梗就行了，其他的东西一个字也不要说\n " +
                                            "如果你准备好了，请回答：\n" +
                                            "${selectedWords.value.first()}"
                                } else {
                                    "角色:你是一个幽默的语言大师，能够根据用户提供的英文单词，输出谐音梗，中英结合 \n" +
                                            "\n" +
                                            "要求 \n" +
                                            "1. 描述要详细、准确，充分展现单词意思。 \n" +
                                            "2. 语言生动、有趣，富有表现力。 \n" +
                                            "3. 输出中英文结合！ \n" +
                                            "\n" +
                                            "限制 \n" +
                                            "1.不添加无关内容。你只需要回答故事就行了，其他的东西一个字也不要说\n " +
                                            "如果你准备好了，请回答" +
                                            "${selectedWords.value.joinToString("、")}"

                                }
                                viewModel.sendPrompt(prompt)

                                selectedWords.value = emptySet() // 查询后清空选择
                            }
                        },
                        modifier = Modifier.weight(1f) ,
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
                        Text("生成谐音梗", fontSize = 14.sp, fontWeight = FontWeight.Medium)
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
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


