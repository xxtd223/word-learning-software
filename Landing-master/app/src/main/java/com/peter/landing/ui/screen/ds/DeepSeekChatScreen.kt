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
    var userInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 聊天历史记录
        ChatHistory(
            history = viewModel.uiState.fullResponse,
            currentResponse = viewModel.uiState.currentResponse,
            isLoading = viewModel.uiState.isLoading,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 输入区域
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(fontSize = 16.sp),
                maxLines = 3,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (userInput.isEmpty()) {
                            Text(
                                text = "输入你的问题...",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (userInput.isNotBlank()) {
                        viewModel.sendPrompt(userInput)
                        userInput = ""
                    }
                },
                enabled = userInput.isNotBlank() && !viewModel.uiState.isLoading
            ) {
                Text("发送")
            }
        }
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
                    text = "开始与DeepSeek对话...",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            } else {
                val messages = history.split("\n").filter { it.isNotBlank() }

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

                    items(messages.reversed()) { message ->
                        val isUser = message.startsWith("用户:")
                        ChatBubble(
                            text = message.substringAfter(":").trim(),
                            isUser = isUser
                        )
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