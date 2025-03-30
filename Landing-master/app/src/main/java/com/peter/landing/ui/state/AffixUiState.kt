package com.peter.landing.ui.state

import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixCatalog

sealed interface AffixUiState {

    object Loading : AffixUiState

    data class Success(
        val affixCatalogType: AffixCatalog.Type,
        val affixMap: Map<AffixCatalog, List<Affix>>
    ) : AffixUiState

}