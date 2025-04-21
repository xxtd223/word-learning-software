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



@Composable
fun HomophonyChatScreen(
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
                text = "请选择单词查询谐音梗:",
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
                            "角色:你是一个幽默的语言大师，能够根据用户提供的英文单词，输出谐音梗，中英结合 \n" +
                                    "\n" +
                                    "要求 \n" +
                                    "1. 描述要详细、准确，充分展现单词意思。 \n" +
                                    "2. 语言生动、有趣，富有表现力。 \n" +
                                    "3. 输出中英文结合！ \n" +
                                    "\n" +
                                    "限制 \n" +
                                    "1.不添加无关内容。你只需要回答谐音梗就行了，其他的东西一个字也不要说\n " +
                                    "如果你准备好了，请回答：\n"+
                                    "${selectedWords.value.first()}"
                        }
                        else {
                            "角色:你是一个幽默的语言大师，能够根据用户提供的英文单词，输出谐音梗，中英结合 \n" +
                                    "\n" +
                                    "要求 \n" +
                                    "1. 描述要详细、准确，充分展现单词意思。 \n" +
                                    "2. 语言生动、有趣，富有表现力。 \n" +
                                    "3. 输出中英文结合！ \n" +
                                    "\n" +
                                    "限制 \n" +
                                    "1.不添加无关内容。你只需要回答故事就行了，其他的东西一个字也不要说\n " +
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
        Spacer(modifier = Modifier.height(8.dp))
    }
}


