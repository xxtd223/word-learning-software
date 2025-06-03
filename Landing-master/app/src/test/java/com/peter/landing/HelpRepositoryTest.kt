package com.peter.landing

import com.peter.landing.data.local.help.Help
import com.peter.landing.data.local.help.HelpDAO
import com.peter.landing.data.repository.help.HelpRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HelpRepositoryTest {

    private lateinit var helpDAO: HelpDAO
    private lateinit var helpRepository: HelpRepository

    @Before
    fun setup() {
        helpDAO = mockk()
        helpRepository = HelpRepository(helpDAO)
    }

    @Test
    fun getHelpListByCatalog_returnsExpectedList() = runTest {
        // Arrange
        val catalogId = 101L
        val mockHelpList = listOf(
            Help(catalogId = catalogId, title = "Title1", content = "Content1"),
            Help(catalogId = catalogId, title = "Title2", content = "Content2")
        )
        coEvery { helpDAO.getHelpListByCatalogId(catalogId) } returns mockHelpList

        // Act
        val result = helpRepository.getHelpListByCatalog(catalogId)

        // Assert
        assertEquals(2, result.size)
        assertEquals("Title1", result[0].title)
        assertEquals("Content2", result[1].content)
    }
}
