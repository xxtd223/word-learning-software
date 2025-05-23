package com.peter.landing.data.local.wrong

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import com.peter.landing.data.local.progress.StudyProgress

@Entity(
    tableName = "wrong",
    foreignKeys = [
        ForeignKey(
            entity = StudyProgress::class,
            parentColumns = ["id"],
            childColumns = ["study_progress_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["study_progress_id"],
            name = "index_wrong_study_progress_id"
        )
    ]
)
data class Wrong(
    @PrimaryKey
    @ColumnInfo(name = "word_id")
    val wordId: Long,

    @ColumnInfo(name = "study_progress_id")
    val studyProgressId: Long,

    @ColumnInfo(name = "chosen_wrong")
    val chosenWrong: Boolean = false,

    @ColumnInfo(name = "spelled_wrong")
    var spelledWrong: Boolean = false,
)