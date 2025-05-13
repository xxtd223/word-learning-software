package com.peter.landing.ui.screen.search

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R

@Composable
fun SearchHistoryDateHeader(date: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // 垂直居中对齐
            modifier = Modifier.fillMaxWidth() // 让整个 Row 占满宽度
        ) {
            Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
            Icon(
                painter = painterResource(R.drawable.ic_search_caomei_24dp),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f) // 确保 Text 占据剩余空间
            )
        }
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
    }
}
@Preview(showBackground = true)
@Composable
fun SearchHistoryDateHeaderPreview() {
    MaterialTheme {
        SearchHistoryDateHeader(date = "2025-05-13")
    }
}
