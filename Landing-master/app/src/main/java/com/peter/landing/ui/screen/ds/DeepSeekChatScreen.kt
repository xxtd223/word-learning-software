package com.peter.landing.ui.screen.ds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peter.landing.ui.viewModel.DeepSeekViewModel

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
    viewModel: DeepSeekViewModel = viewModel()
) {
    // 定义10个单词
    val words = listOf(
        "apple", "banana", "orange", "grape", "pear",
        "strawberry", "blueberry", "kiwi", "mango", "pineapple",
        "guitar","band","stage"
    )

    // 使用Set来记录选中的单词，支持多选
    val selectedWords = remember { mutableStateOf(emptySet<String>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 单词选择区域
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "请选择单词查询故事(可多选):",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 使用FlowRow或Grid布局单词按钮
            LazyColumn {
                items(words.chunked(2)) { rowWords ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
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

        // 结果显示区域
        ChatHistory(
            history = viewModel.uiState.fullResponse,
            currentResponse = viewModel.uiState.currentResponse,
            isLoading = viewModel.uiState.isLoading,
            modifier = Modifier.weight(1f)
        )

        // 底部按钮区域
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 清除选择按钮
            Button(
                onClick = { selectedWords.value = emptySet() },
                modifier = Modifier.weight(1f).padding(end = 4.dp),
                enabled = selectedWords.value.isNotEmpty() && !viewModel.uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("清除选择")
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
                                    "如果你准备好了，请回答：\n"+
                                    "${selectedWords.value.first()}"
                        }
                        else {
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
                                    "如果你准备好了，请回答"+
                                    "${selectedWords.value.joinToString("、")}"

                        }
                        viewModel.sendPrompt(prompt)

                        selectedWords.value = emptySet() // 查询后清空选择
                    }
                },
                modifier = Modifier.weight(1f).padding(start = 4.dp),
                enabled = selectedWords.value.isNotEmpty() && !viewModel.uiState.isLoading
            ) {
                Text("查询")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val combinedPrompt = "$description\n\n$mess"
                    viewModel.sendSilentPrompt(combinedPrompt)
                },
                modifier = Modifier.weight(1f).padding(end = 4.dp),
                enabled = (!viewModel.uiState.isLoading) && (mess.isNotEmpty()) && (!viewModel.hiddenUiState.isLoading)
            ) {
                Text("分析故事结构")
            }
            Button(
                onClick = {
                    viewModel.sendR()
                },
                modifier = Modifier.weight(1f).padding(start = 4.dp),
                enabled = viewModel.sendFlag
            ) {
                Text("sendR")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun WordButton(
    word: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = modifier
            .width(150.dp)
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        onClick = onClick
    ) {
        Text(
            text = word,
            color = contentColor,
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center
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
        color = MaterialTheme.colorScheme.surfaceVariant
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
                    if (message.startsWith("用户: ")) {
                        ChatBubble(
                            text = message.removePrefix("用户: "),
                            isUser = true
                        )
                    } else if (message.startsWith("助手: ")) {
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
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
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
        shape = MaterialTheme.shapes.medium,
        color = bubbleColor
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = if (isUser) "你" else "DeepSeek",
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isTyping) "$text..." else text,
                color = textColor
            )
        }
    }
}