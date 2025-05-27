package com.peter.landing.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.domain.DeepSeekService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


data class Word(val word: String, val meaning: String)

@HiltViewModel
class WordReaderViewModel @Inject constructor() : ViewModel() {
    var selectedWord by mutableStateOf<Word?>(null)
        private set

    var isSidebarVisible by mutableStateOf(false)

    var isTranslating by mutableStateOf(false)
        private set

    private val deepSeekService = DeepSeekService()

    // 示例词汇表，可替换为实际内容
    val allWords = mutableStateListOf(
        Word("Jetpack", "A suite of libraries to help developers write Android apps."),
        Word("Compose", "Android’s modern toolkit for building UI."),
        Word("dedication", "The quality of being committed to a task."),
        Word("strategies", "Plans of action designed to achieve a goal.")
    )

    fun onWordClicked(word: String) {
        val found = allWords.find { it.word.equals(word, ignoreCase = true) }
        if (found != null) {
            selectedWord = found
            isTranslating = false
        } else {
            val prompt = "请翻译这个英语单词为中文：$word 要求：不要说其他的，就说他的中文翻译就行"
            selectedWord = Word(word, "")
            isTranslating = true

            viewModelScope.launch {
                val responseBuilder = StringBuilder()
                deepSeekService.streamResponse(emptyList(), prompt).collect { chunk ->
                    responseBuilder.append(chunk)
                }

                val newWord = Word(word, responseBuilder.toString())
                selectedWord = newWord
                allWords.add(newWord) // 添加到缓存列表
                Log.d("fanuo", selectedWord.toString())
                isTranslating = false
            }
        }
    }



    fun dismissDialog() {
        selectedWord = null
        isTranslating = false
    }


    fun toggleSidebar() {
        isSidebarVisible = !isSidebarVisible
    }
}
