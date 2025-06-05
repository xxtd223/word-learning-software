package com.peter.landing

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.plan.StudyPlanDAO
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.repository.plan.StudyPlanRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class StudyPlanRepositoryTest {

    private lateinit var studyPlanDAO: StudyPlanDAO
    private lateinit var studyPlanRepository: StudyPlanRepository

    @Before
    fun setup() {
        studyPlanDAO = mockk()
        studyPlanRepository = StudyPlanRepository(studyPlanDAO)
    }

    @Test
    fun getStudyPlanFlow_emitsCorrectValue() = runTest {
        val expectedPlan = StudyPlan(
                vocabularyName = Vocabulary.Name.BEGINNER,
                vocabularySize = 1000,
                wordListSize = 100,
                startDate = Calendar.getInstance()
        ).apply {
            id = 1L
            finished = false
        }

        every { studyPlanDAO.getStudyPlanFlow() } returns flow {
            emit(expectedPlan)
        }

        val flow = studyPlanRepository.getStudyPlanFlow()

        flow.collect { actualPlan ->
            assertEquals(expectedPlan, actualPlan)
        }
    }

    @Test
    fun getStudyPlan_returnsExpectedPlan() = runTest {
        val expectedPlan = StudyPlan(
                vocabularyName = Vocabulary.Name.INTERMEDIATE,
                vocabularySize = 1200,
                wordListSize = 120,
                startDate = Calendar.getInstance()
        ).apply {
            id = 1L
            finished = false
        }

        coEvery { studyPlanDAO.getStudyPlan() } returns expectedPlan

        val actualPlan = studyPlanRepository.getStudyPlan()

        assertEquals(expectedPlan, actualPlan)
    }

    @Test
    fun upsertStudyPlan_callsDaoWithCorrectArgument() = runTest {
        val plan = StudyPlan(
                vocabularyName = Vocabulary.Name.BEGINNER,
                vocabularySize = 1500,
                wordListSize = 150,
                startDate = Calendar.getInstance()
        ).apply {
            id = 1L
            finished = true
        }

        coEvery { studyPlanDAO.upsertStudyPlan(plan) } just Runs

        studyPlanRepository.upsertStudyPlan(plan)

        coVerify { studyPlanDAO.upsertStudyPlan(plan) }
    }

    @Test
    fun removeStudyPlan_callsDaoDelete() = runTest {
        coEvery { studyPlanDAO.deleteStudyPlan() } just Runs

        studyPlanRepository.removeStudyPlan()

        coVerify { studyPlanDAO.deleteStudyPlan() }
    }
}