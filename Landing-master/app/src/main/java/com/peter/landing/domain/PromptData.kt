package com.peter.landing.domain

import kotlinx.serialization.Serializable

@Serializable
data class PromptData(
    val positive: String,
    val negative: String
)
