package com.peter.landing.ui.state

import androidx.paging.PagingData
import com.peter.landing.ui.screen.search.SearchHistoryItem
import kotlinx.coroutines.flow.Flow

sealed interface SearchUiState {

    object Loading : SearchUiState

    data class Default(
        val spelling: String = "请输入要搜索的单词",
        val suggestionList: List<String> = emptyList(),
        val searchHistory: Flow<PagingData<SearchHistoryItem>>,
        val dialog: Dialog = Dialog.None
    ) : SearchUiState {

        enum class Dialog {
            History, None
        }

    }

}