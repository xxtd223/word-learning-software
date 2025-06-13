package com.peter.landing.domain.service

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.regex.Pattern

object FileService {

    // 获取最新图片的 URL
    suspend fun getLatestImageUrl(context: Context): String? {
        val client = OkHttpClient()
        return try {
            // 获取文件列表
            val request = Request.Builder()
                .url("http://10.27.245.63:8000/")
                .build()

            val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
            val html = response.body?.string()
            println("Received HTML: $html")

            // 解析最新文件名
            html?.let { parseLatestImage(it) }?.let {
                "http://10.27.245.63:8000/$it"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // 解析最新的图片文件名
    private fun parseLatestImage(html: String): String? {
        val pattern = Pattern.compile("href=\"([^\"]+\\.png)\">")
        val matcher = pattern.matcher(html)

        var latestDate = 0
        var latestSeq = 0
        var latestFile = ""

        while (matcher.find()) {
            val fileName = matcher.group(1)
            println("Found file name: $fileName")
            val parts = fileName.split("_")

            // 确保文件名符合预期格式
            if (parts.size == 3) {
                val date = parts[0].toIntOrNull() ?: continue
                val seq = parts[1].toIntOrNull() ?: continue

                // 判断日期和序号，获取最新文件
                when {
                    date > latestDate -> {
                        latestDate = date
                        latestSeq = seq
                        latestFile = fileName
                    }
                    date == latestDate && seq > latestSeq -> {
                        latestSeq = seq
                        latestFile = fileName
                    }
                }
            }
        }
        return if (latestFile.isNotEmpty()) latestFile else null
    }
}