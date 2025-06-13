import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.domain.service.DeepSeekService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SharedDeepSeekViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val deepSeekService = DeepSeekService()
    private val TAG = "SharedDeepSeekViewModel"

    var uiState by mutableStateOf(DeepSeekUiState())
        private set

    var hiddenUiState by mutableStateOf(DeepSeekUiState())
        private set

    private var currentJob: Job? = null

    // 初始化时加载保存的 fullResponse
    init {
        // 从 SavedStateHandle 中恢复 previous fullResponse
        uiState = savedStateHandle.get<DeepSeekUiState>("uiState") ?: DeepSeekUiState()
        hiddenUiState = savedStateHandle.get<DeepSeekUiState>("hiddenUiState") ?: DeepSeekUiState()
    }

    // 保存状态到 SavedStateHandle
    private fun saveState() {
        savedStateHandle.set("uiState", uiState)
        savedStateHandle.set("hiddenUiState", hiddenUiState)
    }

    fun sendPrompt(prompt: String) {
        currentJob?.cancel() // 取消旧的请求

        currentJob = viewModelScope.launch {
            val buffer = StringBuilder()

            // 将历史对话（包括用户输入和助手的回答）传递给 deepSeekService.streamResponse
            val history = uiState.fullResponse.split("\n\n\n").mapNotNull {
                val parts = it.split(": ", limit = 2)
                if (parts.size == 2) parts[1] else null
            }.chunked(2).map {
                it[0] to (it.getOrNull(1) ?: "")
            }

            deepSeekService.streamResponse(history, prompt)
                .onStart {
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

                    //permanentFullResponse += newFullResponse

                    if (cause == null) {
                        // 请求完成
                    } else {
                        // 请求失败
                    }
                }
                .collect { chunk ->
                    buffer.append(chunk)
                    uiState = uiState.copy(currentResponse = buffer.toString())
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