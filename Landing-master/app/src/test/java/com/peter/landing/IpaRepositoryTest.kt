package com.peter.landing

import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.data.local.ipa.IpaDAO
import com.peter.landing.data.repository.ipa.IpaRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IpaRepositoryTest {

    private lateinit var ipaDAO: IpaDAO
    private lateinit var ipaRepository: IpaRepository

    @Before
    fun setUp() {
        ipaDAO = mockk()
        ipaRepository = IpaRepository(ipaDAO)
    }

    @Test
    fun getIpaList_returnsExpectedList() = runTest {
        // Arrange
        val mockList = listOf(
            Ipa(
                type = Ipa.Type.VOWELS,
                text = "iː",
                exampleWordSpelling = "see",
                exampleWordIpa = "siː",
                exampleWordPronName = "UK"
            ).apply { id = 1 },
            Ipa(
                type = Ipa.Type.CONSONANTS,
                text = "p",
                exampleWordSpelling = "pen",
                exampleWordIpa = "pen",
                exampleWordPronName = "US"
            ).apply { id = 2 }
        )
        coEvery { ipaDAO.getIpaList() } returns mockList

        // Act
        val result = ipaRepository.getIpaList()

        // Assert
        assertEquals(2, result.size)
        assertEquals("iː", result[0].text)
        assertEquals("pen", result[1].exampleWordSpelling)
    }
}
