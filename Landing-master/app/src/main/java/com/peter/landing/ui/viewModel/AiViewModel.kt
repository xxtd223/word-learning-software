package com.peter.landing.ui.viewModel

import ChatRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AiViewModel : ViewModel() {
    var chatHistory = mutableListOf<Pair<String, String>>() // 存储聊天记录

    fun sendMessage(userInput: String, onResponse: (String) -> Unit) {
        chatHistory.add(Pair("你", userInput))

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.chat(ChatRequest(userInput))
                chatHistory.add(Pair("AI", response.response))
                onResponse(response.response)
            } catch (e: Exception) {
                onResponse("请求失败: ${e.message}")
            }
        }
    }
}
