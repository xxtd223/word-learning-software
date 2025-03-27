package com.peter.landing.ui.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.ui.about.AboutConnectInfo
import com.peter.landing.ui.about.AboutInfoItem
import com.peter.landing.ui.about.AboutInfoSection
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun AiScreen(
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
) {
    AiContent(
        navigateToTerms = navigateToTerms,
        navigateTo = navigateTo
    )
}

@Composable
private fun AiContent(
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Ai,
            navigateTo = navigateTo,
        )
        val version = @Composable {
            AboutInfoItem(
                title = stringResource(id = R.string.about_version_title),
                text = stringResource(id = R.string.app_version),
            )
        }
        val lastUpdate = @Composable {
            AboutInfoItem(
                title = stringResource(id = R.string.about_date_title),
                text = stringResource(id = R.string.app_last_update),
            )
        }
        AboutInfoSection(
            heading = stringResource(id = R.string.about_app_name),
            infoList = listOf(version, lastUpdate)
        )

        val service = @Composable {
            AboutInfoItem(
                title = stringResource(id = R.string.about_service_terms),
                text = stringResource(id = R.string.about_service_text),
                clickable = true,
                clickAction = { navigateToTerms(Terms.Type.SERVICE) }
            )
        }
        val privacy = @Composable {
            AboutInfoItem(
                title = stringResource(id = R.string.about_privacy_policy_terms),
                text = stringResource(id = R.string.about_privacy_policy_text),
                clickable = true,
                clickAction = { navigateToTerms(Terms.Type.PRIVACY) }
            )
        }
        val acknowledgement = @Composable {
            AboutInfoItem(
                title = stringResource(id = R.string.about_acknowledge_terms),
                text = stringResource(id = R.string.about_acknowledge_text),
                clickable = true,
                clickAction = { navigateToTerms(Terms.Type.ACKNOWLEDGE) }
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        AboutInfoSection(
            heading = stringResource(id = R.string.about_terms),
            infoList = listOf(service, privacy, acknowledgement)
        )

        Spacer(modifier = Modifier.weight(1f))

        AboutConnectInfo(
            motto = stringResource(id = R.string.about_advisement),
            website = stringResource(id = R.string.about_contact)
        )
    }
}