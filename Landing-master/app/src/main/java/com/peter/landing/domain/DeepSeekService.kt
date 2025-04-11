package com.peter.landing.domain

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class DeepSeekService(
    private val apiBase: String = "http://10.2.8.77:3000/v1",
    private val apiKey: String = "sk-93nWYhI8SrnXad5m9932CeBdDeDf4233B21d93D217095f22",
    private val model: String = "DeepSeek-R1"
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    fun streamResponse(prompt: String): Flow<String> = callbackFlow {
        val url = "$apiBase/chat/completions"

        val payload = JSONObject().apply {
            put("model", model)
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            })
            put("temperature", 0.7)
            put("max_tokens", 1024)
            put("stream", true)
        }

        val request = withContext(Dispatchers.IO) {
            Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $apiKey")
                .header("Content-Type", "application/json")
                .post(payload.toString().toRequestBody())
                .build()
        }

        val eventSourceFactory = EventSources.createFactory(client)

        val finalResponse = StringBuilder()  // 存储最终回复

        val eventSourceListener = object : EventSourceListener() {
            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                val trimmedData = data.trim()
                Log.d("DeepSeekDebug", "收到完整数据: $trimmedData")

                if (trimmedData == "[DONE]") {
                    // 请求结束时打印最终结果
                    Log.d("DeepSeekDebug", "请求完成，最终回复: $finalResponse")
                    close()
                    return
                }

                try {
                    val jsonObject = JSONObject(trimmedData)
                    val choices = jsonObject.getJSONArray("choices")
                    if (choices.length() > 0) {
                        val delta = choices.getJSONObject(0).getJSONObject("delta")
                        if (delta.has("content")) {
                            val content = delta.getString("content")
//                            finalResponse.append(content)  // 追加内容
                            trySend(content)  // 发送部分内容
                        }
                    }
                } catch (e: Exception) {
                    trySend("\n[解析错误] ${e.message}")
                }
            }

            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: Response?
            ) {
                trySend("\n[API请求错误] ${t?.message}")
                close(t)
            }

            override fun onClosed(eventSource: EventSource) {
                close()
            }
        }

        val eventSource = eventSourceFactory.newEventSource(request, eventSourceListener)

        awaitClose {
            eventSource.cancel()
        }
    }
}
