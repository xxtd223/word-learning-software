package com.peter.landing

import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixDAO
import com.peter.landing.data.repository.affix.AffixRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class AffixRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var affixDAO: AffixDAO

    @InjectMockKs
    lateinit var affixRepository: AffixRepository

    @Before
    fun setUp() {
        // 初始化由 @MockK 注解的字段
        // MockKRule 自动完成，无需显式 init
    }

    @Test
    fun getAffixListByCatalog_returnsExpectedList() = runTest {
        val catalogId = 123L
        val mockAffixList = listOf(
            Affix("prefix", "meaning1", "example1", catalogId).apply { id = 1L },
            Affix("suffix", "meaning2", "example2", catalogId).apply { id = 2L }
        )

        coEvery { affixDAO.getAffixListByCatalogId(catalogId) } returns mockAffixList

        val result = affixRepository.getAffixListByCatalog(catalogId)

        assertEquals(mockAffixList.size, result.size)
        assertEquals(mockAffixList[0].text, result[0].text)
        assertEquals(mockAffixList[1].id, result[1].id)
    }
}
