package com.peter.landing.ui.screen.search

import com.peter.landing.data.local.history.SearchHistory

sealed interface SearchHistoryItem {
    data class ItemSearchHistory(
        val searchHistory: SearchHistory
    ) : SearchHistoryItem

    data class SeparatorSearchDate(
        val searchDate: String
    ) : SearchHistoryItem

}