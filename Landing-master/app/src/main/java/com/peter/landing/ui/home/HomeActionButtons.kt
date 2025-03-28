package com.peter.landing.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.ui.navigation.LandingDestination

@Composable
fun HomeActionButtons(
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonItems = listOf(
        ActionItem(R.drawable.ic_plan_24dp, "学习计划", LandingDestination.Main.Plan.route),
        ActionItem(R.drawable.ic_search_24dp, "搜索", LandingDestination.Main.Search.route),
        ActionItem(R.drawable.ic_note_24dp, "笔记", LandingDestination.Main.Note.route),
        ActionItem(R.drawable.ic_ipa_24dp, "音标", LandingDestination.Main.Ipa.route),
        ActionItem(R.drawable.ic_affix_24dp, "词缀", LandingDestination.Main.Affix.route),
        ActionItem(R.drawable.ic_homophony, "谐音", LandingDestination.Main.Homophony.route),
        ActionItem(R.drawable.ic_story, "故事", LandingDestination.Main.Story.route),
        ActionItem(R.drawable.ic_cartoon, "漫画", LandingDestination.Main.Cartoon.route),
        ActionItem(R.drawable.ic_exam, "真题", LandingDestination.Main.Exam.route),
        ActionItem(R.drawable.ic_image_rec, "图片识别翻译", LandingDestination.Main.ImageRec.route)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(items = buttonItems) { item ->
            ActionButton(
                iconRes = item.iconRes,
                label = item.label,
                onClick = { navigateTo(item.route) }
            )
        }
    }
}

private data class ActionItem(
    val iconRes: Int,
    val label: String,
    val route: String
)

@Composable
private fun ActionButton(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(80.dp).padding(vertical = 8.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = Color.Unspecified,
                modifier = Modifier.size(45.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}