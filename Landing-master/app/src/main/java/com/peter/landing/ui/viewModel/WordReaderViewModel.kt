package com.peter.landing.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// 1. 数据模型
data class WordDefinition(val word: String, val meaning: String)

// 2. ViewModel
class WordReaderViewModel : ViewModel() {
    var selectedWord by mutableStateOf<WordDefinition?>(null)
        private set

    fun onWordClicked(word: String) {
        // 这里你可以异步查词，比如从数据库或API
        selectedWord = WordDefinition(word, lookup(word))
    }

    fun dismissDialog() {
        selectedWord = null
    }

    private fun lookup(word: String): String {
        return when (word.lowercase()) {
            "jetpack" -> "Jetpack：喷气背包。也指 Android 的一套现代组件库。"
            "compose" -> "Compose：构建、组成。Jetpack Compose 是 UI 框架。"
            "android’s" -> "Android’s：Android 的（所有格）"
            "modern" -> "Modern：现代的，新的。"
            "toolkit" -> "Toolkit：工具集。"
            "native" -> "Native：原生的。"
            "ui." -> "UI：用户界面（User Interface）。"

            "rewarding" -> "giving you satisfaction or benefits\\n有回报的、值得做的"
            "dedication" -> "the quality of being committed to a task or purpose\n奉献、专注"
            "immerse" ->"to involve deeply in a particular activity\n沉浸、专心于"
            "strategies" ->"plans of action to achieve a goal\n策略、方法"
            "vocabulary" -> "the set of words known and used by a person\n词汇"
            "context" -> "the situation in which something happens that helps you understand it\n语境、上下文"
            "consistent" -> "always behaving or happening in the same way\n一致的、持续的"
            "realistic" -> "based on what is actually possible\n实际的、现实的"
            "achievements" -> "things done successfully with effort or skill\n成就、成果"

            else -> "暂无释义。"
        }
    }
}