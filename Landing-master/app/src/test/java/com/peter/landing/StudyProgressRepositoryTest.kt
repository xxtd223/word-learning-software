package com.peter.landing

import com.peter.landing.data.local.progress.*
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.repository.progress.StudyProgressRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class StudyProgressRepositoryTest {

    private lateinit var studyProgressDAO: StudyProgressDAO
    private lateinit var studyProgressRepository: StudyProgressRepository

    @Before
    fun setup() {
        studyProgressDAO = mockk()
        studyProgressRepository = StudyProgressRepository(studyProgressDAO)
    }

    @Test
    fun getStudyProgressLatestFlow_emitsCorrectValue() = runTest {
        val expectedProgress = StudyProgress(
                id = 1L,
                vocabularyName = Vocabulary.Name.BEGINNER,
                start = 0,
                wordListSize = 1000
        ).apply {
            learned = 500
            chosen = 300
            spelled = 200
            progressState = ProgressState.LEARN
        }

        every { studyProgressDAO.getStudyProgressLatestFlow() } returns flow {
            emit(expectedProgress)
        }

        val flow = studyProgressRepository.getStudyProgressLatestFlow()

        flow.collect { actualProgress ->
            assertEquals(expectedProgress, actualProgress)
        }
    }

    @Test
    fun getStudyProgressLatest_returnsExpectedProgress() = runTest {
        val expectedProgress = StudyProgress(
                id = 1L,
                vocabularyName = Vocabulary.Name.BEGINNER,
                start = 0,
                wordListSize = 1000
        ).apply {
            learned = 500
            chosen = 300
            spelled = 200
            progressState = ProgressState.LEARN
        }

        coEvery { studyProgressDAO.getStudyProgressLatest() } returns expectedProgress

        val actualProgress = studyProgressRepository.getStudyProgressLatest()

        assertEquals(expectedProgress, actualProgress)
    }

    @Test
    fun getLatestLessonReport_returnsCorrectPercentages() = runTest {
        val wordListSize = 1000
        val currentProgress = StudyProgress(
                id = 1L,
                vocabularyName = Vocabulary.Name.BEGINNER,
                start = 0,
                wordListSize = wordListSize
        ).apply {
            learned = 500
            chosen = 300
            spelled = 200
            progressState = ProgressState.LEARN
        }

        coEvery { studyProgressDAO.getStudyProgressLatest() } returns currentProgress

        val result = studyProgressRepository.getLatestLessonReport(wordListSize)

        assertEquals(3, result.size)
        assertEquals(50.0f, result[0], 0.1f)  // learned percentage
        assertEquals(30.0f, result[1], 0.1f)  // chosen percentage
        assertEquals(20.0f, result[2], 0.1f)  // spelled percentage
    }

    @Test
    fun getLatestLessonReport_returnsZeroWhenNoProgress() = runTest {
        val wordListSize = 1000
        coEvery { studyProgressDAO.getStudyProgressLatest() } returns null

        val result = studyProgressRepository.getLatestLessonReport(wordListSize)

        assertEquals(3, result.size)
        assertEquals(0.0f, result[0], 0.1f)  // learned percentage
        assertEquals(0.0f, result[1], 0.1f)  // chosen percentage
        assertEquals(0.0f, result[2], 0.1f)  // spelled percentage
    }

    @Test
    fun getTotalReport_returnsCorrectReport() = runTest {
        val vocabularySize = 1000
        val completed = 500

        coEvery { studyProgressDAO.getCountSpelledInStudyProgress() } returns completed

        val result = studyProgressRepository.getTotalReport(vocabularySize)

        assertEquals(2, result.size)
        assertEquals(500.0f, result[0], 0.1f)  // completed
        assertEquals(1000.0f, result[1], 0.1f) // total vocabulary size
    }

    @Test
    fun insertStudyProgress_callsInsertOnDAO() = runTest {
        val studyProgress = StudyProgress(
                id = 1L,
                vocabularyName = Vocabulary.Name.BEGINNER,
                start = 0,
                wordListSize = 1000
        )

        coEvery { studyProgressDAO.insertStudyProgress(studyProgress) } just Runs

        studyProgressRepository.insertStudyProgress(studyProgress)

        coVerify { studyProgressDAO.insertStudyProgress(studyProgress) }
    }

    @Test
    fun updateStudyProgress_callsUpdateOnDAO() = runTest {
        val studyProgress = StudyProgress(
                id = 1L,
                vocabularyName = Vocabulary.Name.BEGINNER,
                start = 0,
                wordListSize = 1000
        )

        coEvery { studyProgressDAO.updateStudyProgress(studyProgress) } just Runs

        studyProgressRepository.updateStudyProgress(studyProgress)

        coVerify { studyProgressDAO.updateStudyProgress(studyProgress) }
    }

    @Test
    fun removeStudyProgress_callsDeleteOnDAO() = runTest {
        coEvery { studyProgressDAO.deleteStudyProgress() } just Runs

        studyProgressRepository.removeStudyProgress()

        coVerify { studyProgressDAO.deleteStudyProgress() }
    }
}