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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.screen.ds.DeepSeekChatScreen
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun StoryScreen(
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
    navHostController: NavHostController
) {
    StoryContent(
        navigateToTerms = navigateToTerms,
        navigateTo = navigateTo,
        navHostController = navHostController
    )
}

@Composable
private fun StoryContent(
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Story,
            navigateTo = navigateTo,
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ){
            DeepSeekChatScreen(
                navHostController = navHostController // 传递导航控制器
            )
        }

    }
}