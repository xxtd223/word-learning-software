package com.peter.landing.domain

import kotlinx.serialization.Serializable

@Serializable
data class PromptData(
    val positive: String,
    val negative: String = "lowers, bad anatomy, bad hands, text,error, missing fngers,extra digt ,fewer digits,cropped, wort quality ,low quality,normal quality, jpeg artifacts,signature,watermark, username, blurry, bad feet"
)
