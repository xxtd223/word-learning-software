package com.peter.landing.domain.service

import android.util.Log
import com.peter.landing.domain.model.PromptData
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
        val data = PromptData(
            positive = inputPositive,
            negative = "lowers, bad anatomy, bad hands, error, missing fngers,extra digt ,fewer digits,cropped, wort quality ,low quality,normal quality, jpeg artifacts,signature,watermark, username, blurry, bad feet"
        )
        val json = Json.encodeToString(data)
        Log.d("json是",json)
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        Log.d("request是",requestBody.toString())
        val request = Request.Builder()
            .url("http://10.27.245.63:8001/generate")
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
        .joinToString("\n") { it.trim() }
}

