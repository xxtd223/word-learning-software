package com.peter.landing.ui.study.learn

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.ui.util.ExplainSection

@Composable
fun LearnPager(
    spelling: String,
    ipa: String,
    cnExplain: Map<String, List<String>>,
    enExplain: Map<String, List<String>>,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = spelling,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(vertical = 1.dp))
        Text(
            text = ipa,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            ExplainSection(
                spelling = spelling,
                explain = cnExplain,
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.medium)
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            ExplainSection(
                spelling = spelling,
                explain = enExplain,
                isCN = false,
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.medium)
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxWidth()
            )
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
fun PreviewLearnPager() {
    val spelling = "example"
    val ipa = "/ɪɡˈzɑːmpəl/"

    val cnExplain = mapOf(
        "名词" to listOf("例子", "样本"),
        "动词" to listOf("举例说明")
    )

    val enExplain = mapOf(
        "Noun" to listOf("a representative form or pattern", "something to be imitated"),
        "Verb" to listOf("to serve as an example")
    )

    MaterialTheme {
        LearnPager(
            spelling = spelling,
            ipa = ipa,
            cnExplain = cnExplain,
            enExplain = enExplain
        )
    }
}
