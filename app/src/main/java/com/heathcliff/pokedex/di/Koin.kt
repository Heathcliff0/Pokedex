package com.heathcliff.pokedex.di

import com.heathcliff.pokedex.data.NetworkPokemonRepository
import com.heathcliff.pokedex.data.network.PokemonApiService
import com.heathcliff.pokedex.domain.PokemonRepository
import com.heathcliff.pokedex.presentation.details.PokemonDetailsViewModel
import com.heathcliff.pokedex.presentation.list.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<PokemonApiService> { createPokemonApiService() }
    single<PokemonRepository> { NetworkPokemonRepository(get()) }

    viewModel { PokemonListViewModel(get()) }
    viewModel { PokemonDetailsViewModel(get()) }
}

fun createPokemonApiService(): PokemonApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(PokemonApiService::class.java)
}