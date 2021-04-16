package com.heathcliff.pokedex.presentation.adapter

interface DisplayableItem

data class PokemonItem(
    val id: String,
    val name: String,
    val imageUrl: String
):DisplayableItem

data class BannerItem(
    val text: String
): DisplayableItem