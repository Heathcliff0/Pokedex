package com.heathcliff.pokedex.presentation

import com.heathcliff.pokedex.presentation.adapter.PokemonItem

sealed class MainViewState {
    object LoadingState : MainViewState()
    data class ErrorState(val errorMessage: String) : MainViewState()
    data class ContentState(val items: List<PokemonItem>) : MainViewState()
}