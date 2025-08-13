package com.peter.landing.util

import android.content.Context
import java.util.Properties

object ConfigReader {
    private const val LOCAL_PROPERTIES = "local.properties"

    private var properties: Properties? = null

    fun initialize(context: Context) {
        properties = Properties().apply {
            context.assets.open(LOCAL_PROPERTIES).use { load(it) }
        }
    }

    fun getDeepSeekApiKey(): String = properties?.getProperty("deepseek.api.key") ?: ""
    fun getStableDiffusionUrl(): String =
        "${properties?.getProperty("stable.diffusion.base.url") ?: ""}/generate"
}