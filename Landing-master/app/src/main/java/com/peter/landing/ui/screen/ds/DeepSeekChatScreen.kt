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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peter.landing.ui.viewModel.DeepSeekViewModel

@Composable
fun DeepSeekChatScreen(
    modifier: Modifier = Modifier,
    viewModel: DeepSeekViewModel = viewModel()
) {
    // 定义10个单词
    val words = listOf(
        "apple", "banana", "orange", "grape", "pear",
        "strawberry", "blueberry", "kiwi", "mango", "pineapple"
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
                            "${selectedWords.value.first()}这个词的故事是什么？\n要求：1.这个故事可以有效帮助理解这些词的释义。\n2.这个故事简短易懂。"
                        } else {
                            "${selectedWords.value.joinToString("、")}这两个单词能组成什么故事？\n" +
                                    "要求：1.这个故事可以有效帮助理解这些词的释义。\n" +
                                    "2.这个故事简短易懂。"
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
            if (history.isEmpty() && currentResponse.isEmpty()) {
                Text(
                    text = "查询结果将显示在这里...",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            } else {
                val messages = history.split("\n\n\n").filter { it.isNotBlank() }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    reverseLayout = true
                ) {
                    if (currentResponse.isNotBlank()) {
                        item {
                            ChatBubble(
                                text = currentResponse,
                                isUser = false,
                                isTyping = isLoading
                            )
                        }
                    }
                }
            }

            if (isLoading && currentResponse.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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