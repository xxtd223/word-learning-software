package com.peter.landing.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PromptData(
    val positive: String,
    val negative: String
)
