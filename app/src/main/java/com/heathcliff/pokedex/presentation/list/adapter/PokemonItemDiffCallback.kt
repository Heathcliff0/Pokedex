package com.heathcliff.pokedex.presentation.list.adapter

import androidx.recyclerview.widget.DiffUtil

class PokemonItemDiffCallback : DiffUtil.ItemCallback<PokemonItem>() {
    override fun areItemsTheSame(oldItem: PokemonItem, newItem: PokemonItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PokemonItem, newItem: PokemonItem): Boolean {
        return oldItem == newItem
    }
}