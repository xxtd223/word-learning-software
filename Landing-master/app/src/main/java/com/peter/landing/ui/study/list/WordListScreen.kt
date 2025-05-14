package com.peter.landing.ui.study.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.LandingTopBar
import com.peter.landing.data.local.word.Word

@Composable
fun WordListScreen(
    viewModel: WordListViewModel,
    navigateToLearn: () -> Unit,
    navigateUp: () -> Unit
) {
    WordListContent(
        uiState = viewModel.uiState.value,
        startLearning = viewModel::startLearning,
        navigateToLearn = navigateToLearn,
        navigateUp = navigateUp
    )
}

@Composable
private fun WordListContent(
    uiState: WordListUiState,
    startLearning: () -> Unit,
    navigateToLearn: () -> Unit,
    navigateUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.General.WordList,
            navigateUp = navigateUp
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp) // 调整外边距
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                ) // 背景色和圆角效果
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(16.dp)
                ) // 设置更粗的边框和圆角
                .alpha(0.8f) // 设置透明度
        ) {
            when (uiState) {
                is WordListUiState.Error -> {
                    ErrorNotice(uiState.code)
                }

                is WordListUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }

                is WordListUiState.Success -> {
                    if (uiState.start) {
                        LaunchedEffect(Unit) {
                            navigateToLearn()
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            items(uiState.wordList) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .border(1.dp, MaterialTheme.colorScheme.secondary)
                                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = it.spelling,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    )
                                }

                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically, // 垂直居中对齐
                            horizontalArrangement = Arrangement.Center, // 水平居中对齐
                            modifier = Modifier.fillMaxWidth() // 让整个 Row 占满宽度
                        ) {
                            Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
                            Icon(
                                painter = painterResource(R.drawable.ic_ipa_xuanze_24dp),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(40.dp)
                            )
                            Button(
                                onClick = startLearning,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .height(46.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.start_learning),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                    // 外层 Box，确保 Icon 在中央
                    Box(
                        modifier = Modifier
                            .fillMaxSize() // 包裹整个 Box
                            .wrapContentSize(Alignment.Center) // 使内容居中
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.sdu_logo_1), // 替换为你的图标资源
                            contentDescription = "Central Icon",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .size(300.dp) // 设置图标大小
                                .alpha(0.1f) // 设置透明度，0.3f 表示30%的透明度
                        )
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun WordListContentPreviewSuccess() {
    val fakeWordList = listOf(
        Word("example", "/ɪɡˈzɑːmpəl/", mapOf("n." to listOf("例子")), mapOf("n." to listOf("example")), "example.mp3"),
        Word("apple", "/ˈæp.əl/", mapOf("n." to listOf("苹果")), mapOf("n." to listOf("apple")), "apple.mp3"),
        Word("banana", "/bəˈnɑː.nə/", mapOf("n." to listOf("香蕉")), mapOf("n." to listOf("banana")), "banana.mp3"),
        Word("car", "/kɑːr/", mapOf("n." to listOf("汽车")), mapOf("n." to listOf("car")), "car.mp3")
    )
    WordListContent(
        uiState = WordListUiState.Success(
            wordList = fakeWordList,
            start = false
        ),
        startLearning = {},
        navigateToLearn = {},
        navigateUp = {}
    )
}
