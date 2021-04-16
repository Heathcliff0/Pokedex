package com.heathcliff.pokedex.data

import com.heathcliff.pokedex.domain.PokemonEntity
import com.heathcliff.pokedex.domain.PokemonRepository

class MockPokemonRepository : PokemonRepository {
    private val items = mutableListOf(
            PokemonEntity("1", "bulbasaur", generateUrlFromId(1)),
            PokemonEntity("2", "ivysaur", generateUrlFromId(2)),
            PokemonEntity("3", "venusaur", generateUrlFromId(3), 1),
            PokemonEntity("4", "charmander", generateUrlFromId(4), 1),
            PokemonEntity("5", "charmeleon", generateUrlFromId(5), 2),
            PokemonEntity("6", "charizard", generateUrlFromId(6), 2),
            PokemonEntity("7", "squirtle", generateUrlFromId(7), 2),
            PokemonEntity("8", "wartortle", generateUrlFromId(8), 3),
            PokemonEntity("9", "blastoise", generateUrlFromId(9), 3),
            PokemonEntity("10", "caterpie", generateUrlFromId(10), 3)
    )

    override fun getPokemonList(): List<PokemonEntity> = items

    override fun addNewPokemon(pokemon: PokemonEntity) {
        items.add(pokemon)
    }

    private fun generateUrlFromId(id: Int): String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}