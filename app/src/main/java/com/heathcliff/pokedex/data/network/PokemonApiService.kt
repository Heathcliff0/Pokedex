package com.heathcliff.pokedex.data.network

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

fun createPokemonApiService(): PokemonApiService {
    val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    return retrofit.create(PokemonApiService::class.java)
}

interface PokemonApiService {
    @GET("pokemon")
    fun fetchPokemonList(
            @Query("limit") limit: Int = 20,
            @Query("offset") offset: Int = 0
    ): Single<PokemonListResponse>

    @GET("pokemon/{name}")
    fun fetchPokemonInfo(@Path("name") name: String): Single<PokemonDetailedResponse>
}

data class PokemonListResponse(
        val count: Int,
        val results: List<PokemonPartialResponse>
)

data class PokemonPartialResponse(
        val name: String,
        val url: String
)

data class PokemonDetailedResponse(
        val id: String,
        val name: String,
        val abilities: List<PokemonAbilityData>,
        val stats: List<PokemonStatsData>,
        val types: List<PokemonTypeData>,
        val weight: String,
        val height: String
)

// Abilities
data class PokemonAbilityData(
        val ability: PokemonAbilityDetailsData,
)

data class PokemonAbilityDetailsData(
        val name: String
)

// Stats
data class PokemonStatsData(
        val stat: PokemonStatsDetailData,
        val base_stat: String
)

data class PokemonStatsDetailData(
        val name: String
)

// Types
data class PokemonTypeData(
        val type: PokemonTypeDetailedData
)

data class PokemonTypeDetailedData(
        val name: String
)