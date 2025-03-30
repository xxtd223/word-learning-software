package com.peter.landing.ui.state

import com.peter.landing.data.local.progress.ProgressState

sealed interface StudyState {

    object None: StudyState

    data class Learning(
        val progressState: ProgressState
    ): StudyState

    object PlanFinished: StudyState

}