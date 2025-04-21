package com.peter.landing.ui.screen.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peter.landing.R
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.screen.plan.chart.ProgressReportChart
import com.peter.landing.ui.screen.plan.chart.StudyHistoryChart
import com.peter.landing.ui.screen.plan.chart.TotalReportChart
import com.peter.landing.ui.state.PlanUiState
import com.peter.landing.ui.util.ImageNotice
import com.peter.landing.ui.util.LandingTopBar
import com.peter.landing.ui.viewModel.PlanViewModel
import java.util.*

@Composable
fun PlanScreen(
    isDarkMode: Boolean,
    viewModel: PlanViewModel,
    navigateTo: (String) -> Unit,
) {
    val planUiState by viewModel.planUiState.collectAsStateWithLifecycle()

    PlanContent(
        isDarkMode = isDarkMode,
        planUiState = planUiState,
        dialogUiState = viewModel.dialogUiState.value,
        openNewPlanDialog = viewModel::openNewPlanDialog,
        updateNewPlanVocabulary = viewModel::updateNewPlanVocabulary,
        updateNewPlanStartDate = viewModel::updateNewPlanStartDate,
        updateNewPlanWordListSize = viewModel::updateNewPlanWordListSize,
        completeNewPlan = viewModel::completeNewPlan,
        openDeleteDialog = viewModel::openDeleteDialog,
        deletePlan = viewModel::deletePlan,
        closeDeleteDialog = viewModel::closeDeleteDialog,
        closeNewPlanDialog = viewModel::closeNewPlanDialog,
        navigateTo = navigateTo
    )
}

@Composable
private fun PlanContent(
    isDarkMode: Boolean,
    planUiState: PlanUiState,
    dialogUiState: PlanDialogUiState,
    openNewPlanDialog: () -> Unit,
    updateNewPlanVocabulary: (Vocabulary) -> Unit,
    updateNewPlanStartDate: (Calendar?) -> Unit,
    updateNewPlanWordListSize: (Int) -> Unit,
    completeNewPlan: () -> Unit,
    openDeleteDialog: () -> Unit,
    deletePlan: () -> Unit,
    closeDeleteDialog: () -> Unit,
    closeNewPlanDialog: () -> Unit,
    navigateTo: (String) -> Unit,
) {

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Plan,
            navigateTo = navigateTo,
            actions = {
                if (planUiState is PlanUiState.Existed) {
                    IconButton(
                        onClick = openDeleteDialog
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete_forever_24dp),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (planUiState) {
                is PlanUiState.Empty -> {

                    when (dialogUiState) {
                        is PlanDialogUiState.NewPlan -> {
                            NewPlanDialog(
                                vocabularyList = dialogUiState.vocabularyList,
                                studyAmountList = integerArrayResource(
                                    R.array.word_list_num_entry_values
                                ).toList(),
                                updateNewPlanVocabulary = updateNewPlanVocabulary,
                                updateNewPlanStartDate = updateNewPlanStartDate,
                                updateNewPlanWordListSize = updateNewPlanWordListSize,
                                selectedVocabulary = dialogUiState.vocabulary,
                                selectedWordListSize = dialogUiState.wordListSize,
                                selectedStartDate = dialogUiState.startDate,
                                endDate = dialogUiState.endDate,
                                complete = completeNewPlan,
                                onDismiss = closeNewPlanDialog
                            )
                        }
                        else -> Unit
                    }

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        val image = if (isDarkMode) {
                            R.drawable.plannew
                        } else {
                            R.drawable.plannew
                        }

                        Spacer(modifier = Modifier.weight(1.2f))
                        ImageNotice(
                            imageId = image,
                            text = stringResource(id = R.string.plan_empty_msg),
                            Modifier
                                .weight(3.5f)
                                .fillMaxWidth()
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier
                                .weight(2.3f)
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            FloatingActionButton(
                                onClick = openNewPlanDialog,
                                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = ""
                                )
                            }
                        }
                    }

                }
                is PlanUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is PlanUiState.Existed -> {

                    when (dialogUiState) {
                        is PlanDialogUiState.DeletePlan -> {
                            DeletePlanDialog(
                                deletePlan = deletePlan,
                                onDismiss = closeDeleteDialog
                            )
                        }
                        else -> Unit
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()) // 添加垂直滚动
                    ) {
                        if (planUiState.studyHistory.isNotEmpty()) {
                            Box(
                                modifier = Modifier.padding(16.dp).fillMaxWidth()
                            ) {
                                StudyHistoryChart(
                                    historyData = planUiState.studyHistory.mapIndexed { index, value ->
                                        val calendar = Calendar.getInstance().apply {
                                            add(Calendar.DATE, index - 7) // 从一周前到昨天
                                        }
                                        val date = String.format("%02d-%02d",
                                            calendar.get(Calendar.MONTH) + 1,
                                            calendar.get(Calendar.DAY_OF_MONTH)
                                        )
                                        date to value.toInt()
                                    }
                                )
                            }
                        }
                        Spacer(Modifier.height(if (planUiState.progressReport.isNotEmpty()) 8.dp else 0.dp)) // 动态间距
                        if (planUiState.progressReport.isNotEmpty()) {
                            Box(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                ProgressReportChart(planUiState.progressReport)
                            }
                        }
                        Spacer(Modifier.height(if (planUiState.totalReport.isNotEmpty()) 8.dp else 0.dp))

                        if (planUiState.totalReport.isNotEmpty()) {
                            Box(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                TotalReportChart(
                                    planUiState.studyPlan.vocabularyName,
                                    planUiState.totalReport
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp)) // 底部留出空间
                    }

                }
            }
        }
    }
}
