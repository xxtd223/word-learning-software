package com.peter.landing.ui.home

import androidx.compose.foundation.layout.*
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
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.fillMaxWidth()
        ) {

            ActionButton(
                iconRes = R.drawable.ic_plan_24dp,
                label = "学习计划",
                onClick = { navigateTo(LandingDestination.Main.Plan.route) }
            )

            ActionButton(
                iconRes = R.drawable.ic_search_24dp,
                label = "搜索",
                onClick = { navigateTo(LandingDestination.Main.Search.route) }
            )

            ActionButton(
                iconRes = R.drawable.ic_note_24dp,
                label = "笔记",
                onClick = { navigateTo(LandingDestination.Main.Note.route) }
            )

            ActionButton(
                iconRes = R.drawable.ic_ipa_24dp,
                label = "音标",
                onClick = { navigateTo(LandingDestination.Main.Ipa.route) }
            )

        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ){
            ActionButton(
                iconRes = R.drawable.ic_affix_24dp,
                label = "词缀",
                onClick = { navigateTo(LandingDestination.Main.Affix.route) }
            )

            ActionButton(
                iconRes = R.drawable.ic_homophony,
                label = "谐音",
                onClick = { navigateTo(LandingDestination.Main.Homophony.route) }
            )

            ActionButton(
                iconRes = R.drawable.ic_story,
                label = "故事",
                onClick = { navigateTo(LandingDestination.Main.Story.route) }
            )

            ActionButton(
                iconRes = R.drawable.ic_cartoon,
                label = "漫画",
                onClick = { navigateTo(LandingDestination.Main.Cartoon.route) }
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ){
            //
            ActionButton(
                iconRes = R.drawable.ic_exam,
                label = "真题",
                onClick = { navigateTo(LandingDestination.Main.Exam.route) }
            )

            //
            ActionButton(
                iconRes = R.drawable.ic_image_rec,
                label = "图片识别翻译",
                onClick = { navigateTo(LandingDestination.Main.ImageRec.route) }
            )
        }
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