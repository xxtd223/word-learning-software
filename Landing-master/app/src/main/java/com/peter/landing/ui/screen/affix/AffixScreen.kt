package com.peter.landing.ui.screen.affix

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.state.AffixUiState
import com.peter.landing.ui.util.LandingTopBar
import com.peter.landing.ui.viewModel.AffixViewModel



@Composable
fun AffixScreen(
    viewModel: AffixViewModel,
    navigateTo: (String) -> Unit,
) {
    val affixCatalogTypeTypeMenuState = remember { mutableStateOf(false) }
    AffixContent(
        uiState = viewModel.uiState.value,
        setAffixCatalogType = viewModel::setAffixCatalogType,
        affixCatalogTypeTypeMenuState = affixCatalogTypeTypeMenuState,
        navigateTo = navigateTo,
    )
}
@Composable
private fun AffixContent(
    uiState: AffixUiState,
    setAffixCatalogType: (AffixCatalog.Type) -> Unit,
    affixCatalogTypeTypeMenuState: MutableState<Boolean>,
    navigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize() // Box 占满整个屏幕
            .padding(16.dp) // Optional: 如果你想加点内边距，可以启用这个
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Affix,
            navigateTo = navigateTo,
            actions = {
                Box {
                    IconButton(
                        onClick = { affixCatalogTypeTypeMenuState.value = true }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_affix_xuanze1_24dp),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = affixCatalogTypeTypeMenuState.value,
                        onDismissRequest = { affixCatalogTypeTypeMenuState.value = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_affix_qianzhui_24dp),
                                        contentDescription = "",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(25.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp)) // 图标与文本之间的间距
                                    Text(text = AffixCatalog.Type.PREFIX.cnValue)
                                }
                            },
                            onClick = {
                                setAffixCatalogType(AffixCatalog.Type.PREFIX)
                                affixCatalogTypeTypeMenuState.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_affix_houzhui_24dp),
                                        contentDescription = "",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(25.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp)) // 图标与文本之间的间距
                                    Text(text = AffixCatalog.Type.SUFFIX.cnValue)
                                }
                            },
                            onClick = {
                                setAffixCatalogType(AffixCatalog.Type.SUFFIX)
                                affixCatalogTypeTypeMenuState.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_affix_hunhe_24dp),
                                        contentDescription = "",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(25.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp)) // 图标与文本之间的间距
                                    Text(text = AffixCatalog.Type.MIXED.cnValue)
                                }
                            },
                            onClick = {
                                setAffixCatalogType(AffixCatalog.Type.MIXED)
                                affixCatalogTypeTypeMenuState.value = false
                            }
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
            when (uiState) {
                is AffixUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is AffixUiState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        ) {
                            items(uiState.affixMap.toList()) {
                                AffixSection(
                                    affixCatalog = it.first,
                                    affixList = it.second
                                )
                            }
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
                .alpha(0.2f) // 设置透明度，0.3f 表示30%的透明度
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AffixScreenPreview() {
    // 创建两个模拟的 AffixCatalog 对象
    val prefixCatalog = AffixCatalog(0, AffixCatalog.Type.PREFIX, "Prefix Catalog")
    val suffixCatalog = AffixCatalog(1, AffixCatalog.Type.SUFFIX, "Suffix Catalog")

    // 模拟的 uiState
    val mockUiState = AffixUiState.Success(
        affixCatalogType = AffixCatalog.Type.PREFIX, // 你可以选择一个具体的类型
        affixMap = mapOf(
            prefixCatalog to listOf(
                Affix(text = "pre", meaning = "before", example = "prefix", catalogId = prefixCatalog.id)
            ),
            suffixCatalog to listOf(
                Affix(text = "ly", meaning = "in a certain way", example = "quickly", catalogId = suffixCatalog.id)
            )
        )
    )

    // 模拟的导航函数
    val mockNavigateTo: (String) -> Unit = {}

    // 不需要 ViewModel，直接使用模拟数据
    AffixContent(
        uiState = mockUiState,
        setAffixCatalogType = {},
        affixCatalogTypeTypeMenuState = remember { mutableStateOf(false) },
        navigateTo = mockNavigateTo
    )
}






