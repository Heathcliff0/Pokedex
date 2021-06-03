package com.heathcliff.pokedex.data

import com.heathcliff.pokedex.data.network.PokemonApiService
import com.heathcliff.pokedex.domain.PokemonEntity
import com.heathcliff.pokedex.domain.PokemonRepository
import com.heathcliff.pokedex.presentation.list.adapter.PokemonItem
import com.heathcliff.pokedex.utils.REGEX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkPokemonRepository(
    private val api: PokemonApiService
) : PokemonRepository {

    override suspend fun getPokemonList(offset: Int): List<PokemonItem> =
        withContext(Dispatchers.IO) {
            return@withContext api.fetchPokemonList(offset = offset).results.map {
                val id = REGEX.find(it.url)!!.value
                PokemonItem(id, it.name, generateUrlFromId(id))
            }
        }

    override suspend fun getPokemonGenerationList(generation: Int): List<PokemonItem> =
        withContext(Dispatchers.IO) {
            return@withContext api.fetchPokemonGenerationList(gen = generation).pokemon_species.map {
                val id = REGEX.find(it.url)!!.value
                PokemonItem(id, it.name, generateUrlFromId(id))
            }
        }

    override suspend fun getPokemonTypeList(type: Int): List<PokemonItem> =
        withContext(Dispatchers.IO) {
            return@withContext api.fetchPokemonTypeList(type = type).pokemon.map {
                val id = REGEX.find(it.pokemon.url)!!.value
                PokemonItem(id, it.pokemon.name, generateUrlFromId(id))
            }
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

    override suspend fun checkPokemonExistenceById(id: String) {
        api.fetchPokemonId(id)
    }


    private fun generateUrlFromId(id: String): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

}