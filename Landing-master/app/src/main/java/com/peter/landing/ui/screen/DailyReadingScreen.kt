package com.peter.landing.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.screen.ds.WordReaderScreen
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun DailyReadingScreen(
    navigateTo: (String) -> Unit,
) {
    DailyReadingContent(
        navigateTo = navigateTo
    )
}

@Composable
private fun DailyReadingContent(
    navigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.DailyReading,
            navigateTo = navigateTo,
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ){
            WordReaderScreen()
        }

    }
}