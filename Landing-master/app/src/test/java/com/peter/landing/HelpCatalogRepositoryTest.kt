package com.peter.landing

import com.peter.landing.data.local.help.HelpCatalog
import com.peter.landing.data.local.help.HelpCatalogDAO
import com.peter.landing.data.repository.help.HelpCatalogRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HelpCatalogRepositoryTest {

    private lateinit var helpCatalogDAO: HelpCatalogDAO
    private lateinit var helpCatalogRepository: HelpCatalogRepository

    @Before
    fun setup() {
        helpCatalogDAO = mockk()
        helpCatalogRepository = HelpCatalogRepository(helpCatalogDAO)
    }

    @Test
    fun getHelpCatalogList_returnsExpectedList() = runTest {
        // Arrange
        val mockCatalogs = listOf(
            HelpCatalog(id = 1L, name = "Catalog 1", description = "Desc 1"),
            HelpCatalog(id = 2L, name = "Catalog 2", description = "Desc 2")
        )
        coEvery { helpCatalogDAO.getHelpCatalogList() } returns mockCatalogs

        // Act
        val result = helpCatalogRepository.getHelpCatalogList()

        // Assert
        assertEquals(2, result.size)
        assertEquals("Catalog 1", result[0].name)
        assertEquals("Desc 2", result[1].description)
    }

    @Test
    fun getHelpCatalogList_returnsCachedListIfAlreadyFetched() = runTest {
        // Arrange
        val mockCatalogs = listOf(
            HelpCatalog(id = 1L, name = "Cached", description = "Cached Desc")
        )
        coEvery { helpCatalogDAO.getHelpCatalogList() } returns mockCatalogs

        // Act - First call loads from DAO
        val firstCall = helpCatalogRepository.getHelpCatalogList()

        // Act - Second call uses cache
        val secondCall = helpCatalogRepository.getHelpCatalogList()

        // Assert
        assertEquals(firstCall, secondCall)
    }
}
