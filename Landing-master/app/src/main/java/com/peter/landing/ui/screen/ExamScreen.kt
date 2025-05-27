package com.peter.landing.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.peter.landing.data.local.exam.Exam
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.LandingTopBar
import com.peter.landing.ui.viewModel.ExamUiState
import com.peter.landing.ui.viewModel.ExamViewModel

@Composable
fun ExamScreen(
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
    viewModel: ExamViewModel = hiltViewModel()
) {
    ExamContent(
        uiState = viewModel.uiState.value,
        navigateToTerms = navigateToTerms,
        navigateTo = navigateTo,
        onPrevExam = viewModel::prevExamSet,
        onNextExam = viewModel::nextExamSet
    )
}

@Composable
private fun ExamContent(
    uiState: ExamUiState,
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
    onPrevExam: () -> Unit,
    onNextExam: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Exam,
            navigateTo = navigateTo,
        )
        
        when (uiState) {
            is ExamUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            }
            is ExamUiState.Error -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    ErrorNotice(code = uiState.code)
                }
            }
            is ExamUiState.Success -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    ExamHeader(
                        title = uiState.exam.title,
                        onPrevExam = onPrevExam,
                        onNextExam = onNextExam
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ExamQuestions(
                        exam = uiState.exam,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ExamHeader(
    title: String,
    onPrevExam: () -> Unit,
    onNextExam: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onPrevExam) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "上一套",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        
        IconButton(onClick = onNextExam) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "下一套",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ExamQuestions(
    exam: Exam,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        exam.questions.forEach { question ->
            ExamQuestionItem(question = question)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ExamQuestionItem(
    question: Exam.Question
) {
    val selectedOptions = remember { mutableStateListOf<Int>() }
    val isAnswerShown = remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // 问题文本
        Text(
            text = question.text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 选项
        question.options.forEachIndexed { index, option ->
            val isSelected = selectedOptions.contains(index)
            val isCorrect = question.correctAnswers.contains(index)
            val showCorrectness = isAnswerShown.value
            
            val backgroundColor = when {
                !showCorrectness -> if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                isCorrect -> Color(0xFFDCEDC8)
                isSelected -> Color(0xFFFFCDD2)
                else -> Color.Transparent
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(backgroundColor, MaterialTheme.shapes.small)
                    .toggleable(
                        value = isSelected,
                        enabled = !isAnswerShown.value,
                        role = if (question.type == Exam.QuestionType.SINGLE_CHOICE) Role.RadioButton else Role.Checkbox,
                        onValueChange = {
                            if (question.type == Exam.QuestionType.SINGLE_CHOICE) {
                                selectedOptions.clear()
                                if (it) selectedOptions.add(index)
                            } else {
                                if (it) selectedOptions.add(index) else selectedOptions.remove(index)
                            }
                        }
                    )
                    .padding(12.dp)
            ) {
                val optionSymbol = when (index) {
                    0 -> "A"
                    1 -> "B"
                    2 -> "C"
                    3 -> "D"
                    else -> (index + 1).toString()
                }
                
                if (question.type == Exam.QuestionType.SINGLE_CHOICE) {
                    RadioButton(
                        selected = isSelected,
                        onClick = null,
                        enabled = !isAnswerShown.value
                    )
                } else {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = null,
                        enabled = !isAnswerShown.value
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "$optionSymbol. $option",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 检查答案按钮
        if (!isAnswerShown.value) {
            Button(
                onClick = { isAnswerShown.value = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("检查答案")
            }
        } else {
            // 显示解析
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "答案解析",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val correctOptionsText = question.correctAnswers.map { 
                        when (it) {
                            0 -> "A"
                            1 -> "B"
                            2 -> "C"
                            3 -> "D"
                            else -> (it + 1).toString()
                        }
                    }.joinToString(", ")
                    
                    Text(
                        text = "正确答案: $correctOptionsText",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}