package com.peter.landing.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class Word(val word: String, val meaning: String)

@HiltViewModel
class WordReaderViewModel @Inject constructor() : ViewModel() {
    var selectedWord by mutableStateOf<Word?>(null)
        private set

    var isSidebarVisible by mutableStateOf(false)

    // 示例词汇表，可替换为实际内容
    val allWords = listOf(
        Word("Jetpack", "A suite of libraries to help developers write Android apps."),
        Word("Compose", "Android’s modern toolkit for building UI."),
        Word("dedication", "The quality of being committed to a task."),
        Word("strategies", "Plans of action designed to achieve a goal."),
        // ...
    )

    fun onWordClicked(word: String) {
        val found = allWords.find { it.word.equals(word, ignoreCase = true) }
        if (found != null) {
            selectedWord = found
        } else {
            selectedWord = Word(word,"目前没有释义")
        }
    }

    fun dismissDialog() {
        selectedWord = null
    }

    fun toggleSidebar() {
        isSidebarVisible = !isSidebarVisible
    }
}
