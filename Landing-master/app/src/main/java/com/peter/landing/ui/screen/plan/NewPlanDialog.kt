package com.peter.landing.ui.screen.plan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.peter.landing.R
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.util.calculateDate
import com.peter.landing.util.getTodayDateTime
import com.peter.landing.util.getTomorrowDateTime
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewPlanDialog(
        vocabularyList: List<Vocabulary>,
        studyAmountList: List<Int>,
        updateNewPlanVocabulary: (Vocabulary) -> Unit,
        updateNewPlanStartDate: (Calendar?) -> Unit,
        updateNewPlanWordListSize: (Int) -> Unit,
        selectedStartDate: Calendar?,
        selectedVocabulary: Vocabulary,
        selectedWordListSize: Int,
        endDate: String,
        complete: () -> Unit,
        onDismiss: () -> Unit
) {
    Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
                modifier = Modifier
                        .shadow(24.dp, RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .background (MaterialTheme.colorScheme.surface),
                color = MaterialTheme.colorScheme.surface // 保留原始背景色
        ) {
            Box(
                    modifier = Modifier
                            .fillMaxSize()
                            .background(
                                    brush = Brush.verticalGradient(
                                            colors = listOf(
                                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                                            )
                                    )
                            )
            ) {
                Column(
                        modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(24.dp)
                ) {
                    // Header
                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                            )
                        }
                        Text(
                                text = stringResource(R.string.new_plan_title),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Vocabulary Selection
                    SectionCard(
                            titleRes = R.string.new_plan_vocabulary_title,
                            iconRes = R.drawable.ic_vocabulary_24dp
                    ) {
                        LazyRow(
                                contentPadding = PaddingValues(vertical = 4.dp),
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .height(156.dp)
                        ) {
                            items(vocabularyList) {
                                VocabularyItem(
                                        vocabulary = it,
                                        updateNewPlanVocabulary = updateNewPlanVocabulary
                                )
                                Spacer(modifier = Modifier.padding(end = 8.dp))
                            }
                        }
                    }

                    // Word Amount
                    SectionCard(
                            titleRes = R.string.new_plan_word_list_size_title,
                            iconRes = R.drawable.ic_word_list_size_24dp
                    ) {
                        FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            studyAmountList.forEach { amount ->
                                StudyAmountChip(
                                        amount = amount,
                                        selected = amount == selectedWordListSize,
                                        onClick = { updateNewPlanWordListSize(amount) }
                                )
                                if (amount != studyAmountList.last()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }

                    // Date Selection
                    SectionCard(
                            titleRes = R.string.new_plan_start_date_title,
                            iconRes = R.drawable.ic_date_24dp
                    ) {
                        Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            DateChip(
                                    label = stringResource(R.string.new_plan_today_btn),
                                    selected = selectedStartDate == getTodayDateTime(),
                                    onClick = { updateNewPlanStartDate(getTodayDateTime()) }
                            )
                            DateChip(
                                    label = stringResource(R.string.new_plan_tomorrow_btn),
                                    selected = selectedStartDate == getTomorrowDateTime(),
                                    onClick = { updateNewPlanStartDate(getTomorrowDateTime()) }
                            )
                        }
                    }

                    // Summary
                    SectionCard(
                            titleRes = R.string.new_plan_current_title,
                            iconRes = R.drawable.ic_plan_current_24dp
                    ) {
                        CurrentSelectedDetail(
                                selectedVocabulary = selectedVocabulary,
                                selectedWordListSize = selectedWordListSize,
                                selectedStartDate = selectedStartDate,
                                endDate = endDate,
                                modifier = Modifier.padding(8.dp)
                        )
                    }

                    // Action Buttons
                    Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(top = 24.dp)
                    ) {
                        FilledTonalButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                ),
                                shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                    stringResource(R.string.cancel),
                                    style = MaterialTheme.typography.labelLarge
                            )
                        }

                        ElevatedButton(
                                onClick = complete,
                                modifier = Modifier.weight(1f),
                                enabled = selectedVocabulary != Vocabulary() && selectedWordListSize > 0,
                                colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary,
                                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                    stringResource(R.string.complete),
                                    style = MaterialTheme.typography.labelLarge
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
        titleRes: Int,
        iconRes: Int,
        content: @Composable ColumnScope.() -> Unit
) {
    Card(
            colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)), // 添加边框
            modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Column(
                modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                )
                Text(
                        text = stringResource(titleRes),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VocabularyCard(
        vocabulary: Vocabulary,
        selected: Boolean,
        onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
            if (selected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
    )

    Card(
            onClick = onClick,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                    1.dp,
                    if (selected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f) // 加强边框对比度
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // 添加阴影
    ) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                        .width(120.dp)
                        .padding(12.dp)
        ) {
            Text(
                    text = vocabulary.name.cnValue,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                    text = "${vocabulary.size}词",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StudyAmountChip(
        amount: Int,
        selected: Boolean,
        onClick: () -> Unit
) {
    AssistChip(
            onClick = onClick,
            label = { Text("$amount") },
            colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface
            ),
            border = AssistChipDefaults.assistChipBorder(
                    borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    borderWidth = 1.dp // 添加边框
            ),


    )
}

@Composable
private fun DateChip(
        label: String,
        selected: Boolean,
        onClick: () -> Unit
) {
    Surface(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            color = if (selected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface,
            border = BorderStroke(
                    1.dp,
                    if (selected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f) // 加强边框对比度
            ),
            shadowElevation = 1.dp // 添加阴影
    ) {
        Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun CurrentSelectedDetail(
        selectedVocabulary: Vocabulary,
        selectedWordListSize: Int,
        selectedStartDate: Calendar?,
        endDate: String,
        modifier: Modifier
) {
    val vocabulary = selectedVocabulary.takeIf { it != Vocabulary() }?.let {
        "${it.name.cnValue} - ${it.size}个"
    } ?: "未选择"

    val wordListSize = selectedWordListSize.takeIf { it > 0 }?.let {
        "$it 个"
    } ?: "未选择"

    val start = selectedStartDate?.let { calculateDate(it) } ?: "未选择"
    val end = endDate.ifEmpty { "无" }

    Column(modifier = modifier) {
        PlanDetailText(text = "当前词库：$vocabulary")
        PlanDetailText(text = "每日词量：$wordListSize")
        PlanDetailText(text = "开始日期：$start")
        PlanDetailText(text = "预计结束：$end")
    }
}

@Composable
private fun PlanDetailText(text: String) {
    Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 6.dp)
    )
}