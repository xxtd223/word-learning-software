import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.vocabulary.VocabularyDAO
import com.peter.landing.data.repository.vocabulary.VocabularyRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VocabularyRepositoryTest {

    private lateinit var vocabularyDAO: VocabularyDAO
    private lateinit var vocabularyRepository: VocabularyRepository

    @Before
    fun setup() {
        vocabularyDAO = mockk()
        vocabularyRepository = VocabularyRepository(vocabularyDAO)
    }

    @Test
    fun getVocabularyList_firstCall_fetchesFromDAO() = runTest {
        val expectedVocabulary = Vocabulary(
                name = Vocabulary.Name.BEGINNER,
                size = 100,
                description = "Test Description"
        )

        // 修复1: 使用 coEvery 替代 every (处理挂起函数)
        // 修复2: 使用 listOf 替代 Of
        coEvery { vocabularyDAO.getVocabularyList() } returns listOf(expectedVocabulary)

        val result = vocabularyRepository.getVocabularyList()

        assertEquals(1, result.size)
        assertEquals("四级单词书", result[0].name.cnValue)
        assertEquals(100, result[0].size)
        assertEquals("Test Description", result[0].description)

        // 修复3: 使用 coVerify 替代 verify (验证挂起函数)
        coVerify { vocabularyDAO.getVocabularyList() }
    }

    @Test
    fun getVocabularyList_secondCall_usesCache() = runTest {
        val expectedVocabulary = Vocabulary(
                name = Vocabulary.Name.BEGINNER,
                size = 100,
                description = "Test Description"
        )

        coEvery { vocabularyDAO.getVocabularyList() } returns listOf(expectedVocabulary)

        // 第一次调用
        vocabularyRepository.getVocabularyList()

        // 第二次调用
        val result = vocabularyRepository.getVocabularyList()

        assertEquals(1, result.size)
        assertEquals("四级单词书", result[0].name.cnValue)
        assertEquals(100, result[0].size)
        assertEquals("Test Description", result[0].description)

        // 确保只调用一次 DAO
        coVerify(exactly = 1) { vocabularyDAO.getVocabularyList() }
    }

    @Test
    fun getVocabularyList_whenDAOIsEmpty_returnsEmptyList() = runTest {
        // 修复: 返回空列表
        coEvery { vocabularyDAO.getVocabularyList() } returns emptyList()

        val result = vocabularyRepository.getVocabularyList()

        assertEquals(0, result.size)
        coVerify { vocabularyDAO.getVocabularyList() }
    }

    // 以下两个测试重复了前两个测试的功能，可以删除或保留
    @Test
    fun getVocabularyList_whenRepositoryIsEmpty_shouldUseDAO() = runTest {
        val expectedVocabulary = Vocabulary(
                name = Vocabulary.Name.BEGINNER,
                size = 100,
                description = "Test Description"
        )

        coEvery { vocabularyDAO.getVocabularyList() } returns listOf(expectedVocabulary)

        val result = vocabularyRepository.getVocabularyList()

        assertEquals(1, result.size)
        assertEquals("四级单词书", result[0].name.cnValue)
        assertEquals(100, result[0].size)
        assertEquals("Test Description", result[0].description)

        coVerify { vocabularyDAO.getVocabularyList() }
    }

    @Test
    fun getVocabularyList_shouldReturnCachedData() = runTest {
        val expectedVocabulary = Vocabulary(
                name = Vocabulary.Name.BEGINNER,
                size = 100,
                description = "Test Description"
        )

        coEvery { vocabularyDAO.getVocabularyList() } returns listOf(expectedVocabulary)

        vocabularyRepository.getVocabularyList()
        val cachedResult = vocabularyRepository.getVocabularyList()

        assertEquals(1, cachedResult.size)
        assertEquals("四级单词书", cachedResult[0].name.cnValue)
        assertEquals(100, cachedResult[0].size)
        assertEquals("Test Description", cachedResult[0].description)

        coVerify(exactly = 1) { vocabularyDAO.getVocabularyList() }
    }
}