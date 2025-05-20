package com.peter.landing.ui.screen.search

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.state.SearchUiState
import com.peter.landing.ui.util.LandingKeyboard
import com.peter.landing.ui.util.LandingTopBar
import com.peter.landing.ui.viewModel.SearchViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.tooling.preview.Preview
@Composable
fun BannerCarousel(images: List<Int>) {
    // 当前显示的图片索引
    val currentIndex = remember { mutableStateOf(0) }
    // LazyRow的滚动状态
    val scrollState = rememberLazyListState()

    // 自动滚动的效果
    LaunchedEffect(currentIndex.value) {
        // 延迟一段时间后滚动到下一个图片
        delay(3000) // 每3秒切换一次
        currentIndex.value = (currentIndex.value + 1) % images.size

        // 控制 `LazyRow` 自动滚动到目标项
        scrollState.animateScrollToItem(currentIndex.value)
    }

    // LazyRow来显示轮播图
    LazyRow(
            state = scrollState,
            modifier = Modifier.fillMaxWidth()
    ) {
        items(images) { image ->
            Image(
                    painter = painterResource(id = image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                            .width(370.dp) // 设置每个图片的宽度
                            .height(260.dp)
                            .padding(8.dp)
            )
        }
    }
}


@Composable
fun SearchScreen(
    isDarkMode: Boolean,
    viewModel: SearchViewModel,
    navigateToDefinition: (String) -> Unit,
    navigateTo: (String) -> Unit,
) {
    SearchContent(
        isDarkMode = isDarkMode,
        uiState = viewModel.uiState.value,
        search = viewModel::search,
        write = viewModel::write,
        remove = viewModel::remove,
        setWord = viewModel::setWord,
        removeSearchHistory = viewModel::removeSearchHistory,
        openHistoryDialog = viewModel::openHistoryDialog,
        closeDialog = viewModel::closeDialog,
        navigateToDefinition = navigateToDefinition,
        navigateTo = navigateTo
    )
}

@Composable
private fun SearchContent(
    isDarkMode: Boolean,
    uiState: SearchUiState,
    search: () -> Unit,
    write: (String) -> Unit,
    remove: () -> Unit,
    setWord: (String) -> Unit,
    removeSearchHistory: () -> Unit,
    openHistoryDialog: () -> Unit,
    closeDialog: () -> Unit,
    navigateToDefinition: (String) -> Unit,
    navigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Search,
            navigateTo = navigateTo,
            actions = {
                IconButton(
                    onClick = openHistoryDialog
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search_history_24dp),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is SearchUiState.Default -> {

                    when (uiState.dialog) {
                        SearchUiState.Default.Dialog.History -> {
                            SearchHistoryDialog(
                                searchHistory = uiState.searchHistory,
                                setWord = setWord,
                                removeSearchHistory = removeSearchHistory,
                                onDismiss = closeDialog
                            )
                        }
                        SearchUiState.Default.Dialog.None -> Unit
                    }

                    SearchDefaultContent(
                        isDarkMode = isDarkMode,
                        search = search,
                        spelling = uiState.spelling,
                        write = write,
                        clearAlphabet = remove,
                        suggestionList = uiState.suggestionList,
                        setWord = setWord,
                        navigateToDefinition = navigateToDefinition
                    )
                }
                is SearchUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchDefaultContent(
        isDarkMode: Boolean,
        search: () -> Unit,
        spelling: String,
        write: (String) -> Unit,
        clearAlphabet: () -> Unit,
        suggestionList: List<String>,
        setWord: (String) -> Unit,
        navigateToDefinition: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
            modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
    ) {
        Column(
                modifier = Modifier
                        .weight(5.8f)
                        .fillMaxWidth()
        ) {
            if (spelling == "" || spelling == "请输入要搜索的单词") {
                Spacer(modifier = Modifier.weight(1.3f))

                // 定义轮播图的图片资源
                val images = listOf(
                        R.drawable.pangding,
                        R.drawable.pangding,
                        R.drawable.pangding
                )

                // 调用 BannerCarousel 来显示轮播图
                BannerCarousel(images = images)
            } else {
                FlowRow(
                        modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                ) {
                    suggestionList.forEach { suggestion ->
                        OutlinedButton(
                                onClick = { setWord(suggestion) },
                                contentPadding = PaddingValues(8.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                                modifier = Modifier.padding(4.dp)
                        ) {
                            Text(text = suggestion)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically, // 垂直居中对齐
            modifier = Modifier.fillMaxWidth() // 让整个 Row 占满宽度
        ) {
            //Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
            Icon(
                painter = painterResource(R.drawable.ic_search_cat_24dp),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f)
                            .height(48.dp)
                            .background(
                            Color(0xFFE3E3E3).copy(alpha = 0.4f),
                            shape = RoundedCornerShape(4.dp)
                        )
                            .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = MaterialTheme.shapes.extraSmall,
                            )
            ) {
                    Text(
                        text = spelling,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                    )
            }
            Button(
                    onClick = {
                        search()
                        navigateToDefinition(spelling)
                    },
                    contentPadding = PaddingValues(4.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.size(48.dp)
            ) {
                Icon(
                        painter = painterResource(id = R.drawable.ic_search_launch_28dp),
                        contentDescription = ""
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        LandingKeyboard(
                write = write,
                remove = clearAlphabet,
                modifier = Modifier
                        .padding(bottom = 32.dp)
                        .weight(4.2f)
                        .fillMaxWidth()
        )
    }
}
@Preview(showBackground = true)
@Composable
fun SearchDefaultContentPreview() {
    MaterialTheme {
        SearchDefaultContent(
            isDarkMode = false,
            search = { /* 预览中无需实际搜索 */ },
            spelling = "", // 可替换为 "example" 预览不同分支
            write = {},
            clearAlphabet = {},
            suggestionList = listOf("apple", "banana", "cherry", "date", "elderberry"),
            setWord = {},
            navigateToDefinition = {}
        )
    }
}

