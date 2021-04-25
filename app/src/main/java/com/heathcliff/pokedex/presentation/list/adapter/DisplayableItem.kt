package com.heathcliff.pokedex.presentation.list.adapter

import com.heathcliff.pokedex.domain.PokemonEntity

interface DisplayableItem

data class PokemonItem(
        val id: String,
        val name: String,
        val imageUrl: String
) : DisplayableItem

data class BannerItem(
        val text: String
) : DisplayableItem

fun PokemonEntity.toItem(): PokemonItem = PokemonItem(id, name, imageUrl)