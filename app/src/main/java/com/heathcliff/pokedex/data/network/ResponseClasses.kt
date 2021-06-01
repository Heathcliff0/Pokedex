package com.heathcliff.pokedex.data.network

// For list
data class PokemonListResponse(
    val count: Int,
    val results: List<PokemonPartialResponse>
)

data class PokemonPartialResponse(
    val id: String,
    val name: String,
    val url: String
)

// For one pokemon
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