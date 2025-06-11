package com.peter.landing

import com.peter.landing.data.local.word.Word
import com.peter.landing.data.local.word.WordDAO
import com.peter.landing.data.repository.word.WordRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WordRepositoryTest {

    private lateinit var wordDAO: WordDAO
    private lateinit var repository: WordRepository

    @Before
    fun setup() {
        wordDAO = mockk()
        repository = WordRepository(wordDAO)
    }

    @Test
    fun `searchWord returns word when dao returns data`() = runTest {
        val spelling = "apple"
        val expectedWord = Word(
            spelling = spelling,
            ipa = "ˈæpəl",
            cn = mapOf("noun" to listOf("苹果")),
            en = mapOf("noun" to listOf("fruit")),
            pronName = "UK"
        ).apply { id = 1L }

        coEvery { wordDAO.getWordBySpelling(spelling) } returns expectedWord

        val result = repository.searchWord(spelling)

        assertNotNull(result)
        assertEquals(expectedWord.spelling, result?.spelling)
        assertEquals(expectedWord.ipa, result?.ipa)
        assertEquals(expectedWord.pronName, result?.pronName)

        coVerify(exactly = 1) { wordDAO.getWordBySpelling(spelling) }
    }

    @Test
    fun `searchWord returns null when dao returns null`() = runTest {
        val spelling = "nonexistent"

        coEvery { wordDAO.getWordBySpelling(spelling) } returns null

        val result = repository.searchWord(spelling)

        assertNull(result)
        coVerify(exactly = 1) { wordDAO.getWordBySpelling(spelling) }
    }

    @Test
    fun `getSearchSuggestions returns list of strings`() = runTest {
        val spellingPrefix = "app"
        val expectedSuggestions = listOf("apple", "application", "appetite")

        coEvery { wordDAO.getWordSuggestionsBySimilarSpelling("$spellingPrefix%") } returns expectedSuggestions

        val result = repository.getSearchSuggestions(spellingPrefix)

        assertEquals(expectedSuggestions, result)

        coVerify(exactly = 1) { wordDAO.getWordSuggestionsBySimilarSpelling("$spellingPrefix%") }
    }
}
