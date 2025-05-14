package com.peter.landing.ui.study.choice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.word.Word
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.study.Counter
import com.peter.landing.ui.study.InputButtonRow
import com.peter.landing.ui.study.WrongList
import com.peter.landing.ui.util.DictionaryDialog
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.LandingTopBar
import com.peter.landing.ui.util.ProcessingDialog

@Composable
fun ChoiceScreen(
    isDarkMode: Boolean,
    viewModel: ChoiceViewModel,
    playPron: (String) -> Unit,
    navigateToSpelling: () -> Unit,
    navigateUp: () -> Unit
) {
    ChoiceContent(
        isDarkMode = isDarkMode,
        uiState = viewModel.uiState.value,
        choose = viewModel::choose,
        submit = viewModel::submit,
        getNextWord = viewModel::getNextWord,
        playPron = playPron,
        showWrongList = viewModel::showWrongList,
        hideWrongList = viewModel::hideWrongList,
        openDictionaryDialog = viewModel::openDictionaryDialog,
        closeDialog = viewModel::closeDialog,
        navigateToSpelling = navigateToSpelling,
        navigateUp = navigateUp
    )
}

@Composable
private fun ChoiceContent(
    isDarkMode: Boolean,
    uiState: ChoiceUiState,
    choose: (Int) -> Unit,
    submit: () -> Unit,
    getNextWord: () -> Unit,
    playPron: (String) -> Unit,
    showWrongList: () -> Unit,
    hideWrongList: () -> Unit,
    openDictionaryDialog: (Word) -> Unit,
    closeDialog: () -> Unit,
    navigateToSpelling: () -> Unit,
    navigateUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.General.Choice,
            navigateUp = navigateUp,
            leftSideContent = {
                if (uiState is ChoiceUiState.Success &&
                    (uiState.current + 1) == uiState.totalNum
                    && uiState.submitted
                ) {
                    val actionInfo = if (!uiState.showWrongList) {
                        showWrongList to R.drawable.ic_check_wrong_list_24dp
                    } else {
                        hideWrongList to R.drawable.ic_hide_24dp
                    }
                    IconButton(
                        onClick = actionInfo.first
                    ) {
                        Icon(
                            painter = painterResource(actionInfo.second),
                            contentDescription = "",
                            tint = Color.Unspecified
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
                is ChoiceUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is ChoiceUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is ChoiceUiState.Success -> {
                    val lastOne = (uiState.current + 1) == uiState.totalNum
                    val rightInfo = if (uiState.submitted && lastOne) {
                        R.drawable.ic_start_24dp to navigateToSpelling
                    } else {
                        R.drawable.ic_double_arrow_right_24dp to getNextWord
                    }

                    when (uiState.dialog) {
                        ChoiceUiState.Success.Dialog.Processing -> {
                            ProcessingDialog()
                        }
                        ChoiceUiState.Success.Dialog.Dictionary -> {
                            uiState.clickedWrongWord?.let {
                                DictionaryDialog(
                                    word = it,
                                    playPron = playPron,
                                    onDismiss = closeDialog
                                )
                            }
                        }
                        ChoiceUiState.Success.Dialog.None -> Unit
                    }

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                    ) {
                        if (!uiState.showWrongList) {
                            ExerciseContent(isDarkMode, uiState, choose)
                        } else {
                            WrongList(
                                wrongList = uiState.wrongList,
                                openDictionaryDialog = openDictionaryDialog,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            )
                        }
                        InputButtonRow(
                            pronName = uiState.pronName,
                            pronButtonEnable = !uiState.showWrongList,
                            playPron = playPron,
                            leftButtonEnable = !uiState.submitted && uiState.chosenIndex != -2,
                            leftButtonIconId = R.drawable.ic_submit_24dp,
                            leftButtonAction = submit,
                            rightButtonEnable = uiState.submitted,
                            rightButtonIconId = rightInfo.first,
                            rightButtonAction = rightInfo.second,
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth()
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
                .alpha(0.1f) // 设置透明度，0.3f 表示30%的透明度
        )
    }
}

@Composable
private fun ColumnScope.ExerciseContent(
    isDarkMode: Boolean,
    uiState: ChoiceUiState.Success,
    choose: (Int) -> Unit,
) {
    Counter(
        current = uiState.current + 1,
        totalNum = uiState.totalNum,
        modifier = Modifier.fillMaxWidth()
    )
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        Text(
            text = uiState.spelling,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = uiState.ipa,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(vertical = 2.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            var index = 0
            repeat (2) { i ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    repeat (2) { j ->
                        ChoiceOption(
                            isDarkMode = isDarkMode,
                            index = index,
                            cnExplain = uiState.optionList[index].first,
                            enExplain = uiState.optionList[index].second,
                            choose = choose,
                            correctIndex = uiState.correctIndex,
                            chosenIndex = uiState.chosenIndex,
                            submitted = uiState.submitted,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .border(
                                    1.dp, MaterialTheme.colorScheme.tertiary
                                )
                        )
                        if (j == 0) {
                            Spacer(
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }

                        index++
                    }
                }
                if (i == 0) {
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}
@Preview(showBackground = true, name = "ChoiceContent Only Selection UI")
@Composable
fun PreviewChoiceContentSelectionOnly() {
    val word = Word(
        spelling = "example",
        ipa = "/ɪɡˈzɑːmpəl/",
        cn = mapOf("n." to listOf("例子", "样本")),
        en = mapOf("n." to listOf("an instance serving to illustrate a rule")),
        pronName = "example.mp3"
    ).apply { id = 1L }

    val optionWords = listOf(
        Word("example", "/ɪɡˈzɑːmpəl/", mapOf("n." to listOf("例子")), mapOf("n." to listOf("example")), "example.mp3"),
        Word("apple", "/ˈæp.əl/", mapOf("n." to listOf("苹果")), mapOf("n." to listOf("apple")), "apple.mp3"),
        Word("banana", "/bəˈnɑː.nə/", mapOf("n." to listOf("香蕉")), mapOf("n." to listOf("banana")), "banana.mp3"),
        Word("car", "/kɑːr/", mapOf("n." to listOf("汽车")), mapOf("n." to listOf("car")), "car.mp3")
    )

    val options = optionWords.map { it.cn to it.en }

    val fakeUiState = ChoiceUiState.Success(
        current = 0,
        totalNum = 4,
        wordId = word.id,
        spelling = word.spelling,
        pronName = word.pronName,
        ipa = word.ipa,
        optionList = options,
        correctIndex = 0,
        chosenIndex = -2, // 未选择
        submitted = false, // 未提交
        showWrongList = false, // ❌ 不显示错题
        wrongList = emptyList(),
        clickedWrongWord = null,
        dialog = ChoiceUiState.Success.Dialog.None // ❌ 不显示词典弹窗
    )

    MaterialTheme {
        ChoiceContent(
            isDarkMode = false,
            uiState = fakeUiState,
            choose = {},
            submit = {},
            getNextWord = {},
            playPron = {},
            showWrongList = {},
            hideWrongList = {},
            openDictionaryDialog = {},
            closeDialog = {},
            navigateToSpelling = {},
            navigateUp = {}
        )
    }
}





