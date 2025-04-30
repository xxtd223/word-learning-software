package com.peter.landing.ui.screen.note.pager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.peter.landing.R

@Composable
fun WrongWordNumHeader(num: Long) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {Row(
        verticalAlignment = Alignment.CenterVertically, // 垂直对齐方式，使图标和文本在垂直方向对齐
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
        Icon(
            painter = painterResource(R.drawable.ic_note_zhangyuge_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(25.dp)
        )
        Text(
            text = "课时 - $num",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
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
fun WrongWordNumHeaderPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color.Cyan,
            secondary = Color.Green,
            onBackground = Color.Gray
        )
    ) {
        WrongWordNumHeader(num = 5)
    }
}
