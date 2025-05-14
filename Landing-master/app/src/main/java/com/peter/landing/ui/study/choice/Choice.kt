package com.peter.landing.ui.study.choice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.ui.util.ExplainSection
import androidx.compose.ui.res.painterResource


@Composable
fun ChoiceOption(
    isDarkMode: Boolean,
    index: Int,
    cnExplain: Map<String, List<String>>,
    enExplain: Map<String, List<String>>,
    choose: (Int) -> Unit,
    submitted: Boolean,
    chosenIndex: Int,
    correctIndex: Int,
    modifier: Modifier
) {
    val optionTitle = when (index) {
        0 -> "A"
        1 -> "B"
        2 -> "C"
        else -> "D"
    }

    val titleColor = if (index == chosenIndex) {
        Pair(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
        )
    } else {
        Pair(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }

    val explainBackgroundColor = if (submitted) {
        if (index == correctIndex) {
            if (isDarkMode) {
                Color(0xFF2A742D)
            } else {
                Color(0xFFc2eac2)
            }
        } else {
            if (isDarkMode) {
                Color(0xFF92312A)
            } else {
                Color(0xFFffd2d0)
            }
        }
    } else {
        MaterialTheme.colorScheme.background
    }
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // 垂直居中对齐
            horizontalArrangement = Arrangement.Center, // 水平居中对齐
            modifier = Modifier.fillMaxWidth() // 让整个 Row 占满宽度
        ) {
            Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
            val iconRes = when (index) {
                0 -> R.drawable.ic_choice_mifeng_24dp
                1 -> R.drawable.ic_help_yazi_24dp
                2 -> R.drawable.ic_choice_tuzi_24dp
                else -> R.drawable.ic_search_cat_24dp
            }

            val iconSize = if (index == 0) 25.dp else 20.dp // index 0 使用更大的图标尺寸

            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(iconSize)
            )

            Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
            Text(
                text = optionTitle,
                style = MaterialTheme.typography.titleMedium,
                color = titleColor.second,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable(!submitted) { choose(index) }
                    .background(titleColor.first)
                    .padding(4.dp)
                    .fillMaxWidth()
            )
        }
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
            ExplainSection(
                cnExplain = cnExplain,
                enExplain = enExplain,
                modifier = Modifier
                    .background(explainBackgroundColor)
                    .padding(4.dp)
                    .weight(1f) // 使用 weight 来让 ExplainSection 填充剩余空间
                    .fillMaxWidth()
            )
        }
    }

@Preview(showBackground = true, name = "ChoiceOption Preview")
@Composable
fun PreviewChoiceOption() {
    // 模拟数据
    val cnExplain = mapOf("n." to listOf("例子", "样本"))
    val enExplain = mapOf("n." to listOf("an instance serving to illustrate a rule"))

    // 模拟选项索引和状态
    val chosenIndex = 0
    val correctIndex = 0
    val submitted = false

    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // A 选项的预览
            ChoiceOption(
                isDarkMode = false,
                index = 0,
                cnExplain = cnExplain,
                enExplain = enExplain,
                choose = { /* 选择处理逻辑 */ },
                submitted = submitted,
                chosenIndex = chosenIndex,
                correctIndex = correctIndex,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // B 选项的预览（模拟 B 选项为未选择状态）
            ChoiceOption(
                isDarkMode = false,
                index = 1,
                cnExplain = cnExplain,
                enExplain = enExplain,
                choose = { /* 选择处理逻辑 */ },
                submitted = submitted,
                chosenIndex = chosenIndex,
                correctIndex = correctIndex,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }
    }
}
