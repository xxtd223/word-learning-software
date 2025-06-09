import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.peter.landing.data.repository.pref.PreferencesRepository
import com.peter.landing.data.util.AGREEMENT_PREF
import com.peter.landing.data.util.DataResult
import com.peter.landing.data.util.THEME_MODE_PREF
import com.peter.landing.data.util.ThemeMode
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class PreferencesRepositoryTest {

    @get:Rule
    val testDispatcherRule = CoroutineTestRule()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var preferencesRepository: PreferencesRepository

    private val agreementKey = booleanPreferencesKey(AGREEMENT_PREF)
    private val themeKey = stringPreferencesKey(THEME_MODE_PREF)

    @Before
    fun setup() {
        dataStore = mockk(relaxed = true) {
            // mock updateData，忽略其签名
            coEvery { updateData(any<suspend (Preferences) -> Preferences>()) } coAnswers {
                val transform = firstArg<suspend (Preferences) -> Preferences>()
                val prefs = mockk<Preferences>(relaxed = true)
                transform.invoke(prefs)  // 执行 transform
            }

            // mock data 返回，模拟 Preferences 的返回值
            every { data } returns flowOf(
                    mockk {
                        every { get(agreementKey) } returns false
                        every { get(themeKey) } returns ThemeMode.DEFAULT.name
                    }
            )
        }
        preferencesRepository = PreferencesRepository(dataStore)
    }

    @Test
    fun setTheme_shouldUpdateValue() = runTest {
        val result = preferencesRepository.setTheme(ThemeMode.DARK)
        assertTrue(result is DataResult.Success)
        coVerify(exactly = 1) { dataStore.updateData(any<suspend (Preferences) -> Preferences>()) }
    }

    @Test
    fun setTheme_shouldHandleIOException() = runTest {
        // 模拟 updateData 抛出 IOException
        coEvery { dataStore.updateData(any<suspend (Preferences) -> Preferences>()) } throws IOException("Storage failure")

        val result = preferencesRepository.setTheme(ThemeMode.LIGHT)
        assertTrue(result is DataResult.Error)
        assertEquals(DataResult.Error.Code.IO, (result as DataResult.Error).code)
    }

    // 清理 MockK 状态
    @After
    fun tearDown() {
        unmockkAll()
    }

    // 协程测试调度器规则
    class CoroutineTestRule : TestWatcher() {
        val testDispatcher = StandardTestDispatcher()

        override fun starting(description: Description) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }
}