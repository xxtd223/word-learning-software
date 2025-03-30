package com.peter.landing.ui.state

import com.peter.landing.data.local.ipa.Ipa

sealed interface IpaUiState {

    object Loading : IpaUiState

    data class Success(
        val ipaType: Ipa.Type,
        val ipaList: List<Ipa>
    ) : IpaUiState

}