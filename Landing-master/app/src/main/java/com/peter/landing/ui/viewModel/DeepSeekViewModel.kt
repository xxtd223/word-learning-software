package com.peter.landing.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.domain.DeepSeekService
import com.peter.landing.domain.extract4komaScenes
import com.peter.landing.domain.postToGenerateEndpoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DeepSeekViewModel : ViewModel() {
    private val deepSeekService = DeepSeekService()
    private val TAG = "DeepSeekDebug00"

    var uiState by mutableStateOf(DeepSeekUiState())
        private set

    var hiddenUiState by mutableStateOf(DeepSeekUiState())
        private set

    private var currentJob: Job? = null

    fun sendPrompt(prompt: String) {
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            val buffer = StringBuilder()

            deepSeekService.streamResponse(prompt)
                .onStart {
                    Log.d(TAG, "开始请求，用户输入: $prompt")

                    uiState = uiState.copy(
                        isLoading = true,
                        currentResponse = "",
                        fullResponse = uiState.fullResponse + "\n\n\n用户: $prompt"
                    )
                }
                .onCompletion { cause ->
                    val finalResponse = buffer.toString()
                    val newFullResponse = if (finalResponse.isNotBlank()) {
                        uiState.fullResponse + "\n\n\n助手: $finalResponse"
                    } else {
                        uiState.fullResponse + "\n\n\n助手: （无响应）"
                    }

                    uiState = uiState.copy(
                        isLoading = false,
                        currentResponse = "",
                        fullResponse = newFullResponse
                    )

                    if (cause == null) {
                        Log.d(TAG, "请求完成，最终回复111: $buffer")
                    } else {
                        Log.e(TAG, "请求失败: ${cause.message}")
                    }
                }
                .collect { chunk ->
                    buffer.append(chunk)
                }
        }
    }

    fun sendSilentPrompt(prompt: String) {
        viewModelScope.launch {
            val buffer = StringBuilder()

            deepSeekService.streamResponse(prompt)
                .onStart {
                    Log.d(TAG, "开始静默请求: $prompt")
                    hiddenUiState = hiddenUiState.copy(
                        isLoading = true,
                        currentResponse = "",
                        fullResponse = hiddenUiState.fullResponse + prompt
                    )
                }
                .onCompletion { cause ->
                    val finalResponse = buffer.toString()
                    val newFullResponse = if (finalResponse.isNotBlank()) {
                        hiddenUiState.fullResponse + finalResponse
                    } else {
                        hiddenUiState.fullResponse + "（请求失败）"
                    }

                    hiddenUiState = hiddenUiState.copy(
                        isLoading = false,
                        currentResponse = "",
                        fullResponse = newFullResponse
                    )

                    if (cause == null) {

                        Log.d(TAG, "请求完成，最终回复: $buffer")
//目前故事传递功能正在调试，目前注释掉
//                        viewModelScope.launch {
//                            val result = postToGenerateEndpoint(finalResponse)
//                            Log.d(TAG, "发送到生成接口返回：$result")
//                        }
                    } else {
                        Log.e(TAG, "请求失败: ${cause.message}")
                    }
                }
                .collect { chunk ->
                    buffer.append(chunk)
                }
        }
    }

    fun clearConversation() {
        currentJob?.cancel()
        uiState = DeepSeekUiState()
        hiddenUiState = DeepSeekUiState()
    }
}

data class DeepSeekUiState(
    val isLoading: Boolean = false,
    val currentResponse: String = "",
    val fullResponse: String = ""
)