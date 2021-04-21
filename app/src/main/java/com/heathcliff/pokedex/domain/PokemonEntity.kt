package com.heathcliff.pokedex.domain

data class PokemonEntity(
        val id: String,
        val name: String,
        val imageUrl: String,
        val generation: Int = 0,
        val abilities: List<String> = emptyList()
)