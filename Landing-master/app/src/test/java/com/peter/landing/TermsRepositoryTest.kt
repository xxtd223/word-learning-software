import android.content.res.AssetManager
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.data.repository.terms.TermsRepository
import com.peter.landing.data.util.DataResult
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class TermsRepositoryTest {

    @get:Rule
    val testDispatcherRule = CoroutineTestRule()

    private lateinit var assetManager: AssetManager
    private lateinit var termsRepository: TermsRepository

    @Before
    fun setup() {
        assetManager = mockk(relaxed = true)
        termsRepository = TermsRepository(assetManager)
    }

    @Test
    fun getTerms_shouldReadFileSuccessfully() = runTest {
        val jsonString = """
            {
                "type": "SERVICE",
                "data": [
                    { "type": "TITLE", "text": "Terms of Service Title" },
                    { "type": "TEXT", "text": "Terms of Service Content" }
                ]
            }
        """
        val inputStream = MockFileInputStream(jsonString)
        every { assetManager.open("service_terms.json") } returns inputStream

        val result = termsRepository.getTerms(Terms.Type.SERVICE)

        assertTrue(result is DataResult.Success)
        val terms = (result as DataResult.Success).data
        assertEquals(Terms.Type.SERVICE, terms.type)
        assertEquals(2, terms.data.size)
        assertEquals("Terms of Service Title", terms.data[0].text)

        assertTrue(inputStream.isClosed)
    }

    @Test
    fun getTerms_shouldReadFileOnlyOnceWhenCached() = runTest {
        val jsonString = """
            {
                "type": "SERVICE",
                "data": [
                    { "type": "TITLE", "text": "Terms of Service Title" },
                    { "type": "TEXT", "text": "Terms of Service Content" }
                ]
            }
        """
        val inputStream = MockFileInputStream(jsonString)
        every { assetManager.open("service_terms.json") } returns inputStream

        val result1 = termsRepository.getTerms(Terms.Type.SERVICE)
        assertTrue(result1 is DataResult.Success)

        val result2 = termsRepository.getTerms(Terms.Type.SERVICE)
        assertTrue(result2 is DataResult.Success)

        verify(exactly = 1) { assetManager.open("service_terms.json") }
        assertTrue(inputStream.isClosed)
    }

    @Test
    fun getTerms_shouldHandleIOException() = runTest {
        every { assetManager.open("service_terms.json") } throws IOException("File not found")

        val result = termsRepository.getTerms(Terms.Type.SERVICE)

        assertTrue(result is DataResult.Error)
        assertEquals(DataResult.Error.Code.IO, (result as DataResult.Error).code)
    }

    @Test
    fun getTerms_shouldHandleUnknownException() = runTest {
        every { assetManager.open("service_terms.json") } throws IllegalArgumentException("Unknown error")

        val result = termsRepository.getTerms(Terms.Type.SERVICE)

        assertTrue(result is DataResult.Error)
        assertEquals(DataResult.Error.Code.UNKNOWN, (result as DataResult.Error).code)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    class CoroutineTestRule : TestWatcher() {
        val testDispatcher = StandardTestDispatcher()

        override fun starting(description: Description) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }

    private class MockFileInputStream(private val content: String) : java.io.InputStream() {
        private var index = 0
        var isClosed = false

        override fun read(): Int {
            if (isClosed) throw IllegalStateException("Stream already closed")
            return if (index < content.length) {
                content[index++].code
            } else {
                -1
            }
        }

        override fun close() {
            isClosed = true
            super.close()
        }
    }
}