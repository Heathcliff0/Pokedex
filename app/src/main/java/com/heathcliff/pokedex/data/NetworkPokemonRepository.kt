package com.heathcliff.pokedex.data

import com.heathcliff.pokedex.data.network.PokemonApiService
import com.heathcliff.pokedex.domain.PokemonEntity
import com.heathcliff.pokedex.domain.PokemonRepository
import io.reactivex.Observable
import io.reactivex.Single

class NetworkPokemonRepository(
        val api: PokemonApiService
) : PokemonRepository {

    override fun getPokemonList(): Single<List<PokemonEntity>> {
        return api.fetchPokemonList()
                .flatMap { pokemonListResponse ->
                    Observable.fromIterable(pokemonListResponse.results)
                            .flatMapSingle { pokemonPartialResponse ->
                                getPokemonById(pokemonPartialResponse.name)
                            }
                            .toList()
                }
    }

    override fun getPokemonById(id: String): Single<PokemonEntity> {
        return api.fetchPokemonInfo(id).map { pokemonDetailedResponse ->
            val abilities = pokemonDetailedResponse.abilities.map { it.ability.name }

            PokemonEntity(
                    id = pokemonDetailedResponse.id,
                    name = pokemonDetailedResponse.name,
                    imageUrl = generateUrlFromId(pokemonDetailedResponse.id),
                    abilities = abilities
            )
        }
    }

    private fun generateUrlFromId(id: String): String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

}