package com.heathcliff.pokedex.data

import com.heathcliff.pokedex.data.network.PokemonApiService
import com.heathcliff.pokedex.domain.PokemonEntity
import com.heathcliff.pokedex.domain.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkPokemonRepository(
    private val api: PokemonApiService
) : PokemonRepository {

    override suspend fun getPokemonList(): List<PokemonEntity> = withContext(Dispatchers.IO) {
        return@withContext api.fetchPokemonList().results.map { getPokemonById(it.name) }
    }

    override suspend fun getPokemonById(id: String): PokemonEntity = withContext(Dispatchers.IO) {
        api.fetchPokemonInfo(id).let { pokemonDetailedResponse ->
            val abilities = pokemonDetailedResponse.abilities.map { it.ability.name }
            val types = pokemonDetailedResponse.types.map { it.type.name }

            val stats = mutableMapOf<String, String>()
            pokemonDetailedResponse.stats.map {
                stats.put(it.stat.name, it.base_stat)
            }

            return@let PokemonEntity(
                id = pokemonDetailedResponse.id,
                name = pokemonDetailedResponse.name,
                imageUrl = generateUrlFromId(pokemonDetailedResponse.id),
                abilities = abilities,
                stats = stats,
                types = types,
                weight = pokemonDetailedResponse.weight,
                height = pokemonDetailedResponse.height
            )
        }
    }

    private fun generateUrlFromId(id: String): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

}