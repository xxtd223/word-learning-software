package com.peter.landing.ui.screen.ds

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
    // 渐变背景配置（保持原有主题色基础上叠加）
    val gradientBrush = Brush.linearGradient(
            colors = listOf(
                    Color(0xFF6200EA).copy(alpha = 0.4f), // 紫色
                    Color(0xFFFF4081).copy(alpha = 0.4f)  // 粉色
            ),
            start = Offset(0f, 0f),
            end = Offset(1000f, 1000f)
    )

    val articles = listOf(
        EnglishReadingPassages.PASSAGE_1,
        EnglishReadingPassages.PASSAGE_2,
        EnglishReadingPassages.PASSAGE_3,
        EnglishReadingPassages.PASSAGE_4,
        EnglishReadingPassages.PASSAGE_5,
        EnglishReadingPassages.PASSAGE_6,
        EnglishReadingPassages.PASSAGE_7,
        EnglishReadingPassages.PASSAGE_8,
        EnglishReadingPassages.PASSAGE_9,
        EnglishReadingPassages.PASSAGE_10
    )

    val article = remember { articles.random() } // 只会在初始组合时随机一次

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

    Box(
            modifier = Modifier
                    .fillMaxSize()
            .background(brush = gradientBrush))
    {

        // 新增的白色半透明底层（先绘制）
        Box(
                modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 1.0f)) // 60%不透明度的白色
        )
    // 渐变层（叠加在白色层上）
        Box(
            modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradientBrush)
        )
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
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.9f) // 半透明白色背景
                    )
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
                                    lineHeight = 30.sp,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.3.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Justify
                            )
                    )
                }
            }
        }

        // 中间弹窗
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
                        shape = RoundedCornerShape(24.dp),
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 12.dp
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
                    colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 1.0f) // 半透明背景
                    ),
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

                    // 使用 LazyColumn 实现上下滑动
                    LazyColumn(
                            modifier = Modifier.fillMaxHeight()
                    ) {
                        items(viewModel.allWords) { word ->
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
                                Divider(
                                        modifier = Modifier.padding(top = 8.dp),
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

object EnglishReadingPassages {
    // 1. The Importance of Sleep
    val PASSAGE_1 = """
        Adequate sleep is essential for physical and mental health. Studies show that adults need 7-9 hours of sleep daily, 
        yet many sacrifice sleep due to work or entertainment. Lack of sleep weakens immunity, impairs memory, 
        and increases the risk of chronic diseases like diabetes. During sleep, the brain removes toxins 
        and consolidates memories, which is crucial for learning. To improve sleep quality, 
        experts recommend avoiding screens before bed and maintaining a consistent schedule.
    """.trimIndent()

    // 2. The Rise of Remote Work
    val PASSAGE_2 = """
        Remote work has become increasingly popular since the COVID-19 pandemic. Many employees enjoy its flexibility, 
        while companies save on office costs. However, challenges include isolation and blurred work-life boundaries. 
        Surveys indicate that 60% of workers prefer hybrid models—combining home and office work. 
        To adapt, businesses must invest in digital tools and prioritize mental health support.
    """.trimIndent()

    // 3. Plastic Pollution Crisis
    val PASSAGE_3 = """
        Over 8 million tons of plastic enter oceans yearly, harming marine life and ecosystems. 
        Microplastics even enter human food chains, posing health risks. While recycling helps, 
        only 9% of plastic is recycled globally. Solutions include reducing single-use plastics 
        and developing biodegradable alternatives. Governments must enforce stricter regulations to address this urgent issue.
    """.trimIndent()

    // 4. Benefits of Bilingualism
    val PASSAGE_4 = """
        Learning a second language enhances cognitive abilities, such as problem-solving and multitasking. 
        Bilingual individuals often have better memory and delayed dementia onset. Additionally, 
        it boosts career opportunities in a globalized world. Despite the effort required, 
        the long-term advantages make it worthwhile.
    """.trimIndent()

    // 5. Social Media's Impact on Mental Health
    val PASSAGE_5 = """
        Excessive social media use correlates with anxiety and depression, especially among teens. 
        Constant comparison with others' curated lives lowers self-esteem. Yet, platforms also connect people 
        and spread awareness. Experts advise limiting screen time and engaging in offline activities to maintain balance.
    """.trimIndent()

    // 6. The Value of Volunteering
    val PASSAGE_6 = """
        Volunteering benefits both society and individuals. It builds skills, expands networks, and fosters empathy. 
        Research shows volunteers report higher happiness levels. Whether tutoring children or aiding disaster relief, 
        contributing time creates meaningful change.
    """.trimIndent()

    // 7. Urban Green Spaces
    val PASSAGE_7 = """
        Parks and gardens improve city life by reducing air pollution and stress. They also encourage physical activity 
        and social interaction. Cities like Singapore integrate nature into urban planning, 
        setting an example for sustainable development.
    """.trimIndent()

    // 8. Fast Fashion's Environmental Cost
    val PASSAGE_8 = """
        Cheap, trendy clothing generates massive waste and pollution. The industry accounts for 10% of global carbon emissions. 
        Alternatives like thrifting or supporting eco-friendly brands can mitigate harm. 
        Consumers must embrace sustainable fashion choices.
    """.trimIndent()

    // 9. The Power of Gratitude
    val PASSAGE_9 = """
        Practicing gratitude—such as keeping a journal—reduces stress and increases resilience. 
        Studies link gratitude to stronger relationships and life satisfaction. 
        Simple acts, like thanking others, can shift perspectives positively.
    """.trimIndent()

    // 10. Space Exploration's Future
    val PASSAGE_10 = """
        Private companies like SpaceX aim to colonize Mars, while NASA focuses on lunar research. 
        Critics argue funds should address Earth's problems, but space technology often yields everyday innovations (e.g., GPS). 
        Balancing priorities is key.
    """.trimIndent()
}