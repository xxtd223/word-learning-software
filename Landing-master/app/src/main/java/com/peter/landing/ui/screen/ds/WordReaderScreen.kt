package com.peter.landing.ui.screen.ds

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.peter.landing.ui.viewModel.WordReaderViewModel

@Composable
fun WordReaderScreen(
    viewModel: WordReaderViewModel = hiltViewModel(),
) {
    val article = """
        Jetpack Compose is Android’s modern toolkit for building native UI.
        Learning English can be a rewarding experience, but it requires dedication and smart strategies.
    """.trimIndent()

    val selectedWord = viewModel.selectedWord
    val isSidebarVisible = viewModel.isSidebarVisible
    val annotatedText = buildAnnotatedString {
        val regex = Regex("""\w+['’]?\w*|[.,!?]""")
        regex.findAll(article).forEach { match ->
            val word = match.value
            pushStringAnnotation("WORD", word)
            val isSelected = word == selectedWord?.word
            withStyle(
                SpanStyle(
                    background = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            ) {
                append(word)
            }
            pop()
            append(" ")
        }
    }

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📖 Word Reader",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                )
                IconButton(onClick = { viewModel.toggleSidebar() }) {
                    Icon(Icons.Default.List, contentDescription = "All Words")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    ClickableText(
                        text = annotatedText,
                        onClick = { offset ->
                            annotatedText.getStringAnnotations("WORD", offset, offset)
                                .firstOrNull()?.let { viewModel.onWordClicked(it.item) }
                        },
                        style = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.3.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Justify
                        )
                    )
                }
            }
        }

        // 中间弹窗：显示选中的单词解释
        selectedWord?.let {
            val dialogKey = viewModel.isTranslating

            key(dialogKey) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissDialog() },
                    confirmButton = {
                        TextButton(onClick = { viewModel.dismissDialog() }) {
                            Text("Got it")
                        }
                    },
                    title = {
                        Text(
                            text = "📘 ${it.word}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    text = {
                        if (viewModel.isTranslating) {
                            Text(
                                text = "⏳ 正在翻译中...",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 18.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        } else {
                            Text(
                                text = it.meaning,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 18.sp,
                                    lineHeight = 26.sp
                                )
                            )
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                )
            }
        }

        // 右侧滑出词汇表
        AnimatedVisibility(
            visible = isSidebarVisible,
            enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)),
            exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Card(
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📚 All Words",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        IconButton(onClick = { viewModel.toggleSidebar() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close Sidebar")
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    viewModel.allWords.forEach { word ->
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = word.word,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = word.meaning,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

