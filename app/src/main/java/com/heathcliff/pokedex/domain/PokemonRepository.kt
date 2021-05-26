package com.heathcliff.pokedex.domain

interface PokemonRepository {
    suspend fun getPokemonList(): List<PokemonEntity>
    suspend fun getPokemonById(id: String): PokemonEntity
}