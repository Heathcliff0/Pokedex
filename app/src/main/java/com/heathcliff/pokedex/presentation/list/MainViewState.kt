package com.heathcliff.pokedex.presentation.list

import com.heathcliff.pokedex.presentation.list.adapter.PokemonItem

sealed class MainViewState {
    object LoadingState : MainViewState()
    data class ErrorState(val errorMessage: String) : MainViewState()
    data class ContentState(val items: List<PokemonItem>) : MainViewState()
}