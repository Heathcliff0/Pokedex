package com.heathcliff.pokedex.utils

import com.heathcliff.pokedex.databinding.FragmentPokemonDetailsBinding

class ItemViewTypes {
    companion object {
        const val ITEM_VIEW_TYPE_POKEMON = 0
        const val ITEM_VIEW_TYPE_BANNER = 1
    }
}

fun setStats(binding: FragmentPokemonDetailsBinding, stats: Map<String, String>) {
    binding.hpBar.progressText = "HP: ${stats.getValue("hp").toInt()}"
    binding.hpBar.setProgress(stats.getValue("hp").toInt())

    binding.atkBar.progressText = "ATK: ${stats.getValue("attack").toInt()}"
    binding.atkBar.setProgress(stats.getValue("attack").toInt())

    binding.defBar.progressText = "DEF: ${stats.getValue("defense").toInt()}"
    binding.defBar.setProgress(stats.getValue("defense").toInt())

    binding.spdBar.progressText = "SPD: ${stats.getValue("speed").toInt()}"
    binding.spdBar.setProgress(stats.getValue("speed").toInt())
}