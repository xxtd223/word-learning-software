package com.peter.landing

import androidx.paging.PagingSource
import com.peter.landing.data.local.history.SearchHistory
import com.peter.landing.data.local.history.SearchHistoryDAO
import com.peter.landing.data.repository.history.SearchHistoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import kotlinx.coroutines.flow.first
import org.junit.Before
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class SearchHistoryRepositoryTest {

    private lateinit var searchHistoryDAO: SearchHistoryDAO
    private lateinit var searchHistoryRepository: SearchHistoryRepository

    @Before
    fun setUp() {
        searchHistoryDAO = mockk()
        searchHistoryRepository = SearchHistoryRepository(searchHistoryDAO)
    }

    @Test
    fun getTotalSearchHistoryNum_returnsCorrectCount() = runTest {
        // Arrange
        coEvery { searchHistoryDAO.countSearchHistory() } returns 42

        // Act
        val count = searchHistoryRepository.getTotalSearchHistoryNum()

        // Assert
        assertEquals(42, count)
        coVerify(exactly = 1) { searchHistoryDAO.countSearchHistory() }
    }

    @Test
    fun insertSearchHistory_callsDAOWithCorrectData() = runTest {
        // Arrange
        val history = SearchHistory(input = "hello", searchDate = Calendar.getInstance())
        coEvery { searchHistoryDAO.insertSearchHistory(history) } returns Unit

        // Act
        searchHistoryRepository.insertSearchHistory(history)

        // Assert
        coVerify(exactly = 1) { searchHistoryDAO.insertSearchHistory(history) }
    }

    @Test
    fun removeSearchHistory_callsDAODelete() = runTest {
        // Arrange
        coEvery { searchHistoryDAO.deleteSearchHistoryList() } returns Unit

        // Act
        searchHistoryRepository.removeSearchHistory()

        // Assert
        coVerify(exactly = 1) { searchHistoryDAO.deleteSearchHistoryList() }
    }

}
