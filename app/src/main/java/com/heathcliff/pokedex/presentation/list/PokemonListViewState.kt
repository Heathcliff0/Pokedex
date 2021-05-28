package com.heathcliff.pokedex.presentation.list

import com.heathcliff.pokedex.presentation.list.adapter.PokemonItem

sealed class PokemonListViewState {
    object Loading : PokemonListViewState()
    object Error : PokemonListViewState()
    data class Data(val items: List<PokemonItem>) : PokemonListViewState()
}