package com.peter.landing.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.ui.navigation.LandingDestination

@Composable
fun HomeActionButtons(
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth()
    ) {
        // 1. 学习按钮
        ActionButton(
            iconRes = R.drawable.ic_help_24dp,
            label = "帮助",
            onClick = { navigateTo(LandingDestination.Main.Help.route) }
        )

        // 2. 选择题按钮
        ActionButton(
            iconRes = R.drawable.ic_about_24dp,
            label = "关于",
            onClick = { navigateTo(LandingDestination.Main.About.route) }
        )

        // 3. 拼写按钮
        ActionButton(
            iconRes = R.drawable.ic_ai,
            label = "AI",
            onClick = { navigateTo(LandingDestination.Main.Ai.route) }
        )
    }
}

@Composable
private fun ActionButton(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(80.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}