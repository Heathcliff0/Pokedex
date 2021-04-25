package com.heathcliff.pokedex.di

import com.heathcliff.pokedex.data.NetworkPokemonRepository
import com.heathcliff.pokedex.data.network.createPokemonApiService

object Injector {
    fun providePokemonRepository() = NetworkPokemonRepository(api = createPokemonApiService())
}