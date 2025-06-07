package com.peter.landing.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.repository.word.WordRepository
import com.peter.landing.domain.DeepSeekService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


data class Word(val word: String, val meaning: String)

@HiltViewModel
class WordReaderViewModel @Inject constructor(
    private val wordRepository: WordRepository,
) : ViewModel() {
    var selectedWord by mutableStateOf<Word?>(null)
        private set

    var isSidebarVisible by mutableStateOf(false)

    var isTranslating by mutableStateOf(false)
        private set

    private val deepSeekService = DeepSeekService()

    // 示例词汇表，可替换为实际内容
    val allWords = mutableStateListOf(
        Word("Blend", "混合或调和"),
        Word("Crave", "渴望某物"),
        Word("Damp", "微湿的"),
        Word("Frown", "皱眉"),
        Word("Grin", "咧嘴笑"),
        Word("Huddle", "挤作一团"),
        Word("Jot", "快速记下"),
        Word("Kneel", "跪下"),
        Word("Mend", "修补或愈合"),
        Word("Nibble", "小口啃咬"),
        Word("Pat", "轻拍"),
        Word("Quiver", "轻微颤抖"),
        Word("Rust", "生锈"),
        Word("Sip", "小口啜饮"),
        Word("Tidy", "整洁的"),
        Word("Unfold", "展开或显露"),
        Word("Wipe", "擦拭"),
        Word("Yawn", "打哈欠")
    )

    fun onWordClicked(word: String) {
        // 先从缓存中找
        val cached = allWords.find { it.word.equals(word, ignoreCase = true) }
        if (cached != null) {
            Log.d("WordReaderViewModel", "缓存中有")
            selectedWord = cached
            isTranslating = false
            return
        }

        // 缓存没找到，进入异步流程查数据库或AI
        selectedWord = Word(word, "") // 暂时设置选中词（UI 可显示加载状态）
        isTranslating = true

        viewModelScope.launch {
            // 再查数据库
            val dbWord = wordRepository.searchWord(word)

            if (dbWord != null) {
                Log.d("WordReaderViewModel", "数据库中有：${dbWord.spelling} 、 ${dbWord.cn} ")
                selectedWord = Word(dbWord.spelling,dbWord.cn.toString())
                allWords.add(Word(dbWord.spelling,dbWord.cn.toString())) // 加入缓存
                Log.d("WordReaderViewModel", "数据库命中单词加入缓存")
                isTranslating = false
            } else {
                // 最后一步：AI 翻译
                val prompt = "请翻译这个英语单词为中文：$word 要求：不要说其他的，就说它的中文翻译就行"
                val responseBuilder = StringBuilder()

                deepSeekService.streamResponse(emptyList(), prompt).collect { chunk ->
                    responseBuilder.append(chunk)
                }

                val newWord = Word(word, responseBuilder.toString().trim())
                selectedWord = newWord
                allWords.add(newWord)

                Log.d("WordReaderViewModel", "AI 翻译结果已缓存并入库：$newWord")
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
