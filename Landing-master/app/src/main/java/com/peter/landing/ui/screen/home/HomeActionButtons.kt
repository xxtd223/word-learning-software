package com.peter.landing.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
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
            ActionItem(R.drawable.ic_camera, "图片翻译", LandingDestination.Main.ImageTranslation.route)
    )

    Surface(
            modifier = modifier
                    .fillMaxWidth()
                    .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                            MaterialTheme.colorScheme.surface
                                    .copy(alpha = 0.9f)  // 使用标准surface颜色+透明度
                    )
                    .padding(vertical = 8.dp),
            color = Color.Transparent
    ) {
        LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxWidth()
        ) {
            items(items = buttonItems) { item ->
                InteractiveActionButton(
                        iconRes = item.iconRes,
                        label = item.label,
                        onClick = { navigateTo(item.route) }
                )
            }
        }
    }
}

private class DashedShape : Shape {
    override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
    ): Outline {
        return Outline.Generic(
                Path().apply {
                    val step = with(density) { 20.dp.toPx() }
                    var x = 0f
                    while (x < size.width) {
                        moveTo(x, size.height)
                        lineTo(x + step / 2, size.height)
                        x += step
                    }
                }
        )
    }
}

@Composable
private fun InteractiveActionButton(
        iconRes: Int,
        label: String,
        onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                    .width(80.dp)
                    .padding(vertical = 8.dp)
                    .then(
                            if (isPressed) Modifier.border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    shape = DashedShape()
                            ) else Modifier
                    )
    ) {
        IconButton(
                onClick = onClick,
                interactionSource = interactionSource,
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private data class ActionItem(
        val iconRes: Int,
        val label: String,
        val route: String
)