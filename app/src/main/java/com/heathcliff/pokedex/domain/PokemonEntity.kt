package com.heathcliff.pokedex.domain

data class PokemonEntity(
        val id: String,
        val name: String,
        val imageUrl: String,
        val abilities: List<String> = emptyList(),
        val stats: Map<String, String> = emptyMap()
)