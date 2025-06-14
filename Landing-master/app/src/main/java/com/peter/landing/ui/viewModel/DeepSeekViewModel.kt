package com.peter.landing.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.peter.landing.data.repository.progress.StudyProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository

import com.peter.landing.domain.service.DeepSeekService
import com.peter.landing.domain.service.postToGenerateEndpoint
import com.peter.landing.domain.component.GlobalTracker

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class DeepSeekViewModel @Inject constructor(
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val studyProgressRepository: StudyProgressRepository
): ViewModel() {
    var spellingList by mutableStateOf(emptyList<String>())
        private set

    private val deepSeekService = DeepSeekService()
    private val TAG = "DeepSeekDebug00"

    var sendFlag by mutableStateOf(false)

    var uiState by mutableStateOf(DeepSeekUiState())
        private set

    var hiddenUiState by mutableStateOf(DeepSeekUiState())
        private set

    private var ss:String = ""
    private var currentJob: Job? = null


    init {
        viewModelScope.launch {
            val progress = studyProgressRepository.getStudyProgressLatest()
            if (progress != null) {
                val wordList = vocabularyViewRepository.getWordList(
                    start = progress.start,
                    wordListSize = progress.wordListSize,
                    vocabularyName = progress.vocabularyName
                )
                spellingList = wordList.map { it.spelling }
                Log.d("Spelling", spellingList.toString())
            }
        }
    }

    private val mutex = Mutex()

    fun sendPrompt(prompt: String) {
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            val buffer = StringBuilder()

            val history = uiState.fullResponse.split("\n\n\n").mapNotNull {
                val parts = it.split(": ", limit = 2)
                if (parts.size == 2) parts[1] else null
            }.chunked(2).map {
                it[0] to (it.getOrNull(1) ?: "")
            }

            deepSeekService.streamResponse(history, prompt)
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

                    //Log.d("StoryPageDebug", "添加前permanentFullResponse: $permanentFullResponse\n")
                    Log.d("StoryPageDebug", "添加的newFullResponse: $buffer\n")
                    // 使用 Mutex 确保静态变量更新是线程安全的
                    mutex.withLock {
                        viewModelScope.launch {
                            // 在请求开始时更新 GlobalTracker
                            GlobalTracker.appendContent("$buffer\n")
                        }
                    }
                    //Log.d("StoryPageDebug", "添加后permanentFullResponse: $permanentFullResponse\n")

                    if (cause == null) {
                        Log.d(TAG, "请求完成，最终回复111: $buffer\n")
                        Log.d(TAG, "finalResponse内容: $finalResponse\n")
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

            val history = uiState.fullResponse.split("\n\n\n").mapNotNull {
                val parts = it.split(": ", limit = 2)
                if (parts.size == 2) parts[1] else null
            }.chunked(2).map {
                it[0] to (it.getOrNull(1) ?: "")
            }

            deepSeekService.streamResponse(history, prompt)
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

                    //permanentFullResponse += newFullResponse

                    if (cause == null) {
                        Log.d(TAG, "请求完成，最终回复: $buffer")
                        ss = finalResponse
                        if (ss.isNotEmpty()) {
                            sendFlag = true
                        }
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

    fun sendR(){
        viewModelScope.launch {
            val result = postToGenerateEndpoint(ss)
            Log.d(TAG, "发送到生成接口返回：$result")
        }
    }
}

data class DeepSeekUiState(
    val isLoading: Boolean = false,
    val currentResponse: String = "",
    val fullResponse: String = ""
)