package com.peter.landing.ui.state

import com.peter.landing.data.local.plan.StudyPlan

sealed interface PlanUiState {

    object Loading : PlanUiState

    data class Existed(
        val studyPlan: StudyPlan,
        val progressReport: List<Float> = emptyList(),
        val totalReport: List<Float> = emptyList(),
        val studyHistory: List<Float> = emptyList(),
    ) : PlanUiState

    object Empty : PlanUiState

}