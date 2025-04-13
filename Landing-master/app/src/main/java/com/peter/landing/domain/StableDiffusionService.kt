package com.peter.landing.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object StableDiffusionService {

    // 向 ComfyUI API 发送正面和负面提示词
    suspend fun sendPromptToComfyUI(positivePrompt: String, negativePrompt: String, onResponse: (String) -> Unit) {
        val url = "http://10.27.245.63:8001/generate"
        val client = OkHttpClient()

        val json = JSONObject().apply {
            put("positive", positivePrompt)
            put("negative", negativePrompt)
        }

        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
            if (response.isSuccessful) {
                val responseData = response.body?.string() ?: "没有返回数据"
                onResponse(responseData)
            } else {
                onResponse("请求失败: ${response.message}")
            }
        } catch (e: Exception) {
            onResponse("请求异常: ${e.message}")
        }
    }
}