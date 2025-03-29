package com.peter.landing.ui.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun AiScreen(
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
    viewModel: AiViewModel = viewModel()
) {
    AiContent(
        navigateToTerms = navigateToTerms,
        navigateTo = navigateTo,
        viewModel = viewModel
    )
}

@Composable
private fun AiContent(
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
    viewModel: AiViewModel
) {
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    val chatHistory by remember { mutableStateOf(viewModel.chatHistory) }

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

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            chatHistory.forEach { (sender, message) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (sender == "你") Arrangement.Start else Arrangement.End
                ) {
                    Text(
                        text = "$sender: $message",
                        modifier = Modifier
                            .background(Color.LightGray)
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (userInput.text.isNotBlank()) {
                        viewModel.sendMessage(userInput.text) {
                            userInput = TextFieldValue("")
                        }
                    }
                }
            ) {
                Text(text = "发送")
            }
        }
    }
}
