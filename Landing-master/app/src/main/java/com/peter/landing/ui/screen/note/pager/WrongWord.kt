package com.peter.landing.ui.screen.note.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.wrong.ProgressWrongWord

@Composable
fun WrongWord(
    progressWrongWord: ProgressWrongWord,
    openExplainDialog: (String, Map<String, List<String>>) -> Unit,
    playPron: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(
                Color(0xFFE3E3E3).copy(alpha = 0.4f),
                shape = RoundedCornerShape(4.dp)
            )
            //.shadow(2.dp, shape = RoundedCornerShape(4.dp), clip = false) // 阴影效果
            .padding(horizontal = 1.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text(
                    text = progressWrongWord.spelling,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                Text(
                    text = progressWrongWord.ipa,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = {
                    openExplainDialog(
                        progressWrongWord.spelling,
                        progressWrongWord.cn
                    )
                },
                modifier = Modifier.width(48.dp)
            ) {
                Text(text = "中")
            }
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            TextButton(
                onClick = {
                    openExplainDialog(
                        progressWrongWord.spelling,
                        progressWrongWord.en
                    )
                },
                modifier = Modifier.width(48.dp)
            ) {
                Text(text = "英")
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(
                onClick = { playPron(progressWrongWord.pronName) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_sound_24dp),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewWrongWord() {
    WrongWord(
        progressWrongWord = ProgressWrongWord(
            studyProgressId = 1L, // 这里添加一个 ID
            spelling = "example",
            ipa = "/ɪɡˈzæmpəl/",
            cn = mapOf("definition" to listOf("示例", "例子")),
            en = mapOf("definition" to listOf("a typical instance of something")),
            pronName = "example_pron"
        ),
        openExplainDialog = { spelling, explanations ->
            // Handle dialog open logic
        },
        playPron = { pronName ->
            // Handle pronunciation logic
        }
    )
}


