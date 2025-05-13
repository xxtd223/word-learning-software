package com.peter.landing.ui.screen.note.pager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.landing.R

@Composable
fun NoteAlphabetHeader(alphabet: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
        verticalAlignment = Alignment.CenterVertically, // 垂直对齐方式，使图标和文本在垂直方向对齐
        modifier = Modifier.fillMaxWidth()
    ) {
            Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
            Icon(
                painter = painterResource(R.drawable.ic_note_xielaoban_24dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(25.dp)
            )
            Text(
                text = alphabet,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            )
        }
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewNoteAlphabetHeader() {
    NoteAlphabetHeader(
        alphabet = "A"  // 使用一个示例字母
    )
}
