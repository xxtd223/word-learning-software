import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// 替换为你的 API 地址
const val BASE_URL = "`https://你的deepseek-api.com/`"

data class ChatRequest(val prompt: String)
data class ChatResponse(val response: String)

interface DeepSeekApi {
    @Headers("Content-Type: application/json")
    @POST("chat") // 具体路径根据你的 API 需求调整
    suspend fun chat(@Body request: ChatRequest): ChatResponse
}

object RetrofitInstance {
    val api: DeepSeekApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeepSeekApi::class.java)
    }
}
