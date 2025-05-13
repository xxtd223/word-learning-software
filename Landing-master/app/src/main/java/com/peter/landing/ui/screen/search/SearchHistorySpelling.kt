package com.peter.landing.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchHistorySpelling(
    spelling: String,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .background(
                Color(0xFFE3E3E3).copy(alpha = 0.4f),
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onClick(spelling) }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = spelling,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Divider(thickness = 1.5.dp, color = MaterialTheme.colorScheme.outline)
    }

}
@Preview(showBackground = true)
@Composable
fun SearchHistorySpellingPreview() {
    MaterialTheme {
        SearchHistorySpelling(
            spelling = "preview-example",
            onClick = { /* 点击时不执行任何操作 */ }
        )
    }
}

