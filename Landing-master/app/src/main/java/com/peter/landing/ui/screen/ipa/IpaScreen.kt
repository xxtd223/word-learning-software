package com.peter.landing.ui.screen.ipa

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.state.IpaUiState
import com.peter.landing.ui.util.LandingTopBar
import com.peter.landing.ui.viewModel.IpaViewModel

@Composable
fun IpaScreen(
    viewModel: IpaViewModel,
    playPron: (String) -> Unit,
    navigateTo: (String) -> Unit,
) {
    val ipaTypeMenuState = remember { mutableStateOf(false) }
    IpaContent(
        uiState = viewModel.uiState.value,
        setIpaType = viewModel::setIpaType,
        ipaTypeMenuState = ipaTypeMenuState,
        playPron = playPron,
        navigateTo = navigateTo
    )
}

@Composable
private fun IpaContent(
    uiState: IpaUiState,
    setIpaType: (Ipa.Type) -> Unit,
    ipaTypeMenuState: MutableState<Boolean>,
    playPron: (String) -> Unit,
    navigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Ipa,
            navigateTo = navigateTo,
            actions = {
                if (uiState is IpaUiState.Success) {
                    Box {
                        IconButton(
                            onClick = { ipaTypeMenuState.value = true }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_ipa_xuanze_24dp),
                                contentDescription = "",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = ipaTypeMenuState.value,
                            onDismissRequest = { ipaTypeMenuState.value = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_ipa_fuyin_24dp),
                                            contentDescription = null,
                                            tint = Color.Unspecified,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
                                        Text(text = Ipa.Type.CONSONANTS.cnValue)
                                    }
                                },
                                onClick = {
                                    setIpaType(Ipa.Type.CONSONANTS)
                                    ipaTypeMenuState.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_ipa_yuanyin_24dp),
                                            contentDescription = null,
                                            tint = Color.Unspecified,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
                                        Text(text = Ipa.Type.VOWELS.cnValue)
                                    }
                                },
                                onClick = {
                                    setIpaType(Ipa.Type.VOWELS)
                                    ipaTypeMenuState.value = false
                                }
                            )
                        }
                    }
                }
            }
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp) // 调整外边距
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp)) // 背景色和圆角效果
                .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp)) // 设置更粗的边框和圆角
                .alpha(0.8f) // 设置透明度
        ) {
            when (uiState) {
                is IpaUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is IpaUiState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_ipa_biaotiqian_24dp),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
                            Text(
                                text = "当前类型：${uiState.ipaType.cnValue}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
                        IpaList(
                            ipaList = uiState.ipaList,
                            playPron = playPron
                        )
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
fun PreviewIpaScreen() {
    // 模拟数据
    val mockIpaList = listOf(
        Ipa(Ipa.Type.CONSONANTS, "b", "ball", "bɔːl", "ball"),
        Ipa(Ipa.Type.VOWELS, "a", "cat", "kæt", "cat")
    )

    // 模拟UI状态
    val mockUiState = IpaUiState.Success(
        ipaType = Ipa.Type.CONSONANTS,
        ipaList = mockIpaList
    )

    // 这里模拟 IpaType 的选择界面
    val ipaTypeMenuState = remember { mutableStateOf(false) }

    // 模拟 playPron 和 navigateTo 的功能
    IpaContent(
        uiState = mockUiState,
        setIpaType = { type ->
            // 模拟设置类型的行为
            println("Selected IPA Type: ${type.cnValue}")
        },
        ipaTypeMenuState = ipaTypeMenuState,
        playPron = { ipa -> println("Playing pronunciation for $ipa") },
        navigateTo = { destination -> println("Navigating to $destination") }
    )
}



