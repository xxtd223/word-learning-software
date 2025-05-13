package com.peter.landing.ui.screen.help

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.state.HelpUiState
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.LandingTopBar
import com.peter.landing.ui.viewModel.HelpViewModel
import com.peter.landing.data.local.help.Help
import com.peter.landing.data.local.help.HelpCatalog

@Composable
fun HelpScreen(
    viewModel: HelpViewModel,
    navigateTo: (String) -> Unit,
) {
    HelpContent(
        uiState = viewModel.uiState.value,
        navigateTo = navigateTo
    )
}

@Composable
private fun HelpContent(
    uiState: HelpUiState,
    navigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
            .padding(16.dp) // Optional: 如果你想加点内边距，可以启用这个
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Help,
            navigateTo = navigateTo,
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is HelpUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is HelpUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is HelpUiState.Success -> {
                    val helpSection = uiState.helpMap.toList()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(helpSection) {
                            HelpSection(
                                helpCatalog = it.first,
                                helpList = it.second
                            )
                        }
                    }

                }
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

@Preview(showBackground = true)
@Composable
fun HelpContentSuccessPreview() {
    val mockHelpMap = mapOf(
        HelpCatalog(
            id = 1,
            name = "Getting Started",
            description = "How to begin using the app"
        ) to listOf(
            Help(catalogId = 1, title = "Welcome", content = "Welcome to the app!"),
            Help(catalogId = 1, title = "Basics", content = "Learn the basics of navigation.")
        )
    )

    HelpContent(
        uiState = HelpUiState.Success(helpMap = mockHelpMap),
        navigateTo = {}
    )
}





