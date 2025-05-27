package com.peter.landing.data.local.exam

@kotlinx.serialization.Serializable
data class Exam(
    val title: String,
    val level: Level,
    val questions: List<Question>
) {
    @kotlinx.serialization.Serializable
    enum class Level {
        CET4, CET6
    }

    @kotlinx.serialization.Serializable
    data class Question(
        val id: Int,
        val type: QuestionType,
        val text: String,
        val options: List<String>,
        val correctAnswers: List<Int>, // 索引列表，对于单选题只有一个元素，多选题可以有多个
        val explanation: String
    )

    @kotlinx.serialization.Serializable
    enum class QuestionType {
        SINGLE_CHOICE, MULTIPLE_CHOICE
    }
} 