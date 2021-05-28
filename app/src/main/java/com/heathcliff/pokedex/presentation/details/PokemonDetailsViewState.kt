package com.heathcliff.pokedex.presentation.details

sealed class PokemonDetailsViewState {
    object Loading : PokemonDetailsViewState()

    data class Data(
        val name: String,
        val imageUrl: String,
        val abilities: List<String>,
        val stats: Map<String, String>,
        val types: List<String>,
        val weight: String,
        val height: String
    ) : PokemonDetailsViewState()

    data class Error(val message: String) : PokemonDetailsViewState()
}