package com.peter.landing.data.local.vocabulary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabulary")
data class Vocabulary(
    @PrimaryKey
    val name: Name,

    val size: Int,

    val description: String
) {
    constructor() : this(Name.NONE, 0, "")

    enum class Name(val cnValue: String) {
        BEGINNER("四级单词书"), INTERMEDIATE("六级单词书"), NONE("")
    }

}