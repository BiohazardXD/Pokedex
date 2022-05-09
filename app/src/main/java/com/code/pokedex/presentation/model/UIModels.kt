package com.code.pokedex.presentation.model

import com.code.pokedex.domain.model.Pokemon

private const val DEFAULT_QUERY = ""

sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()
}

data class UiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)

sealed class UiModel {
    data class PokemonItem(val pokemon: Pokemon) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}