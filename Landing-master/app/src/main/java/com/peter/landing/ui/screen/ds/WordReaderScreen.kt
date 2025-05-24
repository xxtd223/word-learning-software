package com.peter.landing.ui.screen.ds

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
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
        First, immerse yourself in the language. Watch English movies, listen to podcasts, and try to think in English during daily activities.

        Secondly, build a strong vocabulary. Don’t just memorize words—learn them in context.
        Read articles, write journals, and use new words in sentences.

        Practice speaking as much as possible. Find a language partner, join online communities, or talk to yourself in front of a mirror.

        Lastly, be consistent. It’s better to study for twenty minutes every day than two hours once a week.
        Set realistic goals, track your progress, and celebrate your achievements.

        Remember, making mistakes is part of the journey. Stay motivated, stay curious, and enjoy the process of becoming fluent.
    """.trimIndent()

    val selectedWord = viewModel.selectedWord

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
            Text(
                text = "📖 Word Reader",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            )

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

        // ✅ 右侧滑出释义卡片
        AnimatedVisibility(
            visible = selectedWord != null,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(durationMillis = 300)
            ),
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            selectedWord?.let {
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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "📘 ${it.word}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            TextButton(onClick = { viewModel.dismissDialog() }) {
                                Text("✖")
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = it.meaning,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 18.sp,
                                lineHeight = 26.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
