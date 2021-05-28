package com.heathcliff.pokedex.presentation.list.adapter

import com.heathcliff.pokedex.domain.PokemonEntity

data class PokemonItem(
    val id: String,
    val name: String,
    val imageUrl: String
)

fun PokemonEntity.toItem(): PokemonItem = PokemonItem(id, name, imageUrl)