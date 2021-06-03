package com.heathcliff.pokedex.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun fetchPokemonInfo(@Path("name") name: String): PokemonDetailedResponse

    @GET("pokemon/{name}")
    suspend fun fetchPokemonId(@Path("name") name: String): PokemonPartialResponse

    @GET("generation/{gen}")
    suspend fun fetchPokemonGenerationList(@Path("gen") gen: Int): PokemonGenerationList

    @GET("type/{type}")
    suspend fun fetchPokemonTypeList(@Path("type") type: Int): PokemonTypeList
}