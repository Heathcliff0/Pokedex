package com.heathcliff.pokedex.domain

interface PokemonRepository {
    suspend fun getPokemonList(offset: Int): List<PokemonEntity>
    suspend fun getPokemonById(id: String): PokemonEntity
    suspend fun checkPokemonExistenceById(id: String)
}