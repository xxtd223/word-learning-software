package com.peter.landing.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.domain.DeepSeekService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DeepSeekViewModel : ViewModel() {
    private val deepSeekService = DeepSeekService()
    private val TAG = "DeepSeekDebug00" // 定义日志标签

    var uiState by mutableStateOf(DeepSeekUiState())
        private set

    private var currentJob: Job? = null

    fun sendPrompt(prompt: String) {
        currentJob?.cancel() // 取消之前的请求

        currentJob = viewModelScope.launch {
            deepSeekService.streamResponse(prompt)
                .onStart {
                    Log.d(TAG, "开始请求，用户输入: $prompt") // 打印用户输入
                    uiState = uiState.copy(
                        isLoading = true,
                        currentResponse = "",
                        fullResponse = uiState.fullResponse + "\n用户: $prompt"
                    )
                }
                .onCompletion {
                        cause ->
                    if (cause == null) {
                        Log.d(TAG, "请求完成，最终回复: ${uiState.currentResponse}") // 打印最终回复
                    } else {
                        Log.e(TAG, "请求失败: ${cause.message}") // 打印错误
                    }
                    uiState = uiState.copy(
                        isLoading = false,
                        fullResponse = uiState.fullResponse + "\n助手: ${uiState.currentResponse}"
                    )
                }
                .collect { chunk ->
                    uiState = uiState.copy(
                        currentResponse = uiState.currentResponse + chunk
                    )
                }
        }
    }

    fun clearConversation() {
        currentJob?.cancel()
        uiState = DeepSeekUiState()
    }
}

data class DeepSeekUiState(
    val isLoading: Boolean = false,
    val currentResponse: String = "",
    val fullResponse: String = ""
)