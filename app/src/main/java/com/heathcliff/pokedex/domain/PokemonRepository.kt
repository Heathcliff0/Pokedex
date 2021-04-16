package com.heathcliff.pokedex.domain

interface PokemonRepository {
    fun getPokemonList(): List<PokemonEntity>
    fun addNewPokemon(pokemon: PokemonEntity)
}