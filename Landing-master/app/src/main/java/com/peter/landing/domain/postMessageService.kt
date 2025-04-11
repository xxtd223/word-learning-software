package com.peter.landing.domain

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

suspend fun postToGenerateEndpoint(positiveText: String): String? = withContext(Dispatchers.IO) {
    try {
        var inputPositive : String = extract4komaScenes(positiveText)
        val data = PromptData(positive = inputPositive)
        val json = Json.encodeToString(data)

        val client = OkHttpClient()
        val mediaType = "application/json".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://172.20.10.3:8001/generate")
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            return@withContext response.body?.string()
        } else {
            Log.e("DeepSeekDebug00", "请求失败，状态码: ${response.code}")
            return@withContext null
        }
    } catch (e: Exception) {
        Log.e("DeepSeekDebug00", "请求异常: ${e.message}")
        return@withContext null
    }
}

fun extract4komaScenes(input: String): String {
    return input
        .lineSequence() // 逐行处理
        .filter { line ->
            line.trim().startsWith("[4koma]") || line.trim().startsWith("[SCENE-")
        }
        .joinToString("\\n") { it.trim() }
}

