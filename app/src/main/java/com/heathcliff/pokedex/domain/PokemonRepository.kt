package com.heathcliff.pokedex.domain

import com.heathcliff.pokedex.presentation.list.adapter.PokemonItem

interface PokemonRepository {
    suspend fun getPokemonList(offset: Int): List<PokemonItem>
    suspend fun getPokemonGenerationList(generation: Int): List<PokemonItem>
    suspend fun getPokemonTypeList(generation: Int): List<PokemonItem>
    suspend fun getPokemonById(id: String): PokemonEntity
    suspend fun checkPokemonExistenceById(id: String)
}