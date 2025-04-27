package com.peter.landing.ui.screen.ipa

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.landing.R
import com.peter.landing.data.local.ipa.Ipa

@Composable
fun IpaListItem(
    ipa: Ipa,
    playPron: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(
                Color(0xFFE3E3E3).copy(alpha = 0.4f),
                shape = RoundedCornerShape(4.dp)
            )
            //.shadow(2.dp, shape = RoundedCornerShape(4.dp), clip = false) // 阴影效果
            .padding(horizontal = 1.dp)
    ) {
        Text(
            text = ipa.text,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(3f)
        ) {
            Text(
                text = ipa.exampleWordSpelling,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = ipa.exampleWordIpa,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(
            onClick = { playPron(ipa.exampleWordPronName) }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_sound_24dp),
                contentDescription = "Play Pronunciation",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewIpaListItem() {
    val ipa = Ipa(
        type = Ipa.Type.VOWELS,
        text = "æ",
        exampleWordSpelling = "cat",
        exampleWordIpa = "/kæt/",
        exampleWordPronName = "cat_pron"
    )

    IpaListItem(
        ipa = ipa,
        playPron = { pronName -> println("Playing pronunciation for: $pronName") }
    )
}
