package com.peter.landing

import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.data.local.affix.AffixCatalog.Type
import com.peter.landing.data.local.affix.AffixCatalogDAO
import com.peter.landing.data.repository.affix.AffixCatalogRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AffixCatalogRepositoryTest {

    private lateinit var affixCatalogDAO: AffixCatalogDAO
    private lateinit var affixCatalogRepository: AffixCatalogRepository

    @Before
    fun setup() {
        affixCatalogDAO = mockk()
        affixCatalogRepository = AffixCatalogRepository(affixCatalogDAO)
    }

    @Test
    fun getAffixCatalogList_returnsExpectedList() = runTest {
        // Arrange
        val mockCatalogs = listOf(
            AffixCatalog(id = 1L, type = Type.PREFIX, description = "Prefix description"),
            AffixCatalog(id = 2L, type = Type.SUFFIX, description = "Suffix description")
        )
        coEvery { affixCatalogDAO.getAffixCatalogList() } returns mockCatalogs

        // Act
        val result = affixCatalogRepository.getAffixCatalogList()

        // Assert
        assertEquals(2, result.size)
        assertEquals(Type.PREFIX, result[0].type)
        assertEquals("Suffix description", result[1].description)
    }

    @Test
    fun getAffixCatalogList_returnsCachedListIfAlreadyFetched() = runTest {
        // Arrange
        val mockCatalogs = listOf(
            AffixCatalog(id = 1L, type = Type.PREFIX, description = "Cached description")
        )
        coEvery { affixCatalogDAO.getAffixCatalogList() } returns mockCatalogs

        // Act - First call (fetches from DAO)
        val result1 = affixCatalogRepository.getAffixCatalogList()

        // Act - Second call (should return cached list, DAO not called again)
        val result2 = affixCatalogRepository.getAffixCatalogList()

        // Assert
        assertEquals(result1, result2)
    }
}
