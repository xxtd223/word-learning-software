package com.peter.landing

import androidx.paging.PagingSource
import androidx.paging.PagingData
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.local.wrong.ProgressWrongWord
import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.local.wrong.WrongDAO
import com.peter.landing.data.repository.wrong.WrongRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class WrongRepositoryTest {

    private lateinit var wrongDAO: WrongDAO
    private lateinit var repository: WrongRepository

    @Before
    fun setup() {
        wrongDAO = mockk()
        repository = WrongRepository(wrongDAO)
    }

    @After
    fun tearDown() {
        clearMocks(wrongDAO)
    }

    @Test
    fun `getChosenWrong returns expected words`() = runBlocking {
        val studyProgressId = 1L
        val fakeWords = listOf(
            Word("test", "tɛst", mapOf("cn" to listOf("测试")), mapOf("en" to listOf("test")), "pron1").apply { id = 101 }
        )

        coEvery { wrongDAO.getWrongByStudyProgressIdAndChosenWrong(studyProgressId) } returns fakeWords

        val result = repository.getChosenWrong(studyProgressId)

        assertEquals(fakeWords, result)
        coVerify(exactly = 1) { wrongDAO.getWrongByStudyProgressIdAndChosenWrong(studyProgressId) }
    }

    @Test
    fun `getSpelledWrong returns expected words`() = runBlocking {
        val studyProgressId = 2L
        val fakeWords = listOf(
            Word("spell", "spɛl", mapOf("cn" to listOf("拼写")), mapOf("en" to listOf("spell")), "pron2").apply { id = 202 }
        )

        coEvery { wrongDAO.getWrongByStudyProgressIdAndSpelledWrong(studyProgressId) } returns fakeWords

        val result = repository.getSpelledWrong(studyProgressId)

        assertEquals(fakeWords, result)
        coVerify(exactly = 1) { wrongDAO.getWrongByStudyProgressIdAndSpelledWrong(studyProgressId) }
    }

    @Test
    fun `addChosenWrong inserts a wrong with chosenWrong true`() = runBlocking {
        val wordId = 10L
        val studyProgressId = 20L

        coEvery { wrongDAO.insertWrong(any()) } just Runs

        repository.addChosenWrong(wordId, studyProgressId)

        coVerify(exactly = 1) {
            wrongDAO.insertWrong(match {
                it.wordId == wordId && it.studyProgressId == studyProgressId && it.chosenWrong
            })
        }
    }

    @Test
    fun `addSpelledWrong updates existing wrong if found`() = runBlocking {
        val wordId = 30L
        val studyProgressId = 40L
        val existingWrong = Wrong(wordId, studyProgressId, chosenWrong = false, spelledWrong = false)

        coEvery { wrongDAO.getWrongById(wordId) } returns existingWrong
        coEvery { wrongDAO.updateWrong(any()) } just Runs

        repository.addSpelledWrong(wordId, studyProgressId)

        coVerifySequence {
            wrongDAO.getWrongById(wordId)
            wrongDAO.updateWrong(match { it.spelledWrong })
        }
    }

    @Test
    fun `addSpelledWrong inserts new wrong if not found`() = runBlocking {
        val wordId = 50L
        val studyProgressId = 60L

        coEvery { wrongDAO.getWrongById(wordId) } returns null
        coEvery { wrongDAO.insertWrong(any()) } just Runs

        repository.addSpelledWrong(wordId, studyProgressId)

        coVerifySequence {
            wrongDAO.getWrongById(wordId)
            wrongDAO.insertWrong(match {
                it.wordId == wordId && it.studyProgressId == studyProgressId && it.spelledWrong
            })
        }
    }


    @Test
    fun `removeWrong calls deleteWrong`() = runBlocking {
        coEvery { wrongDAO.deleteWrong() } just Runs

        repository.removeWrong()

        coVerify(exactly = 1) { wrongDAO.deleteWrong() }
    }
}


