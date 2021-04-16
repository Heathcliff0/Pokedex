package com.heathcliff.pokedex.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.heathcliff.pokedex.data.MockPokemonRepository
import com.heathcliff.pokedex.domain.PokemonEntity
import com.heathcliff.pokedex.domain.PokemonRepository
import com.heathcliff.pokedex.presentation.adapter.BannerItem
import com.heathcliff.pokedex.presentation.adapter.DisplayableItem
import com.heathcliff.pokedex.presentation.adapter.PokemonItem

class MainViewModel : ViewModel() {
    private val repository: PokemonRepository = MockPokemonRepository()

    private val _pokemonListLiveData = MutableLiveData<List<DisplayableItem>>()
    fun getPokemonList(): LiveData<List<DisplayableItem>> = _pokemonListLiveData

    fun loadData() {
        val pokemons = repository.getPokemonList()
        val resultList = mutableListOf<DisplayableItem>()

        val maxGeneration = pokemons.maxBy { it.generation }!!.generation

        for (generation in 0..maxGeneration) {
            resultList.add(BannerItem("Generation $generation"))
            resultList.addAll(
                pokemons.filter { it.generation == generation }
                    .map { it.toItem() }
            )
        }

        _pokemonListLiveData.value = resultList
    }
}

private fun PokemonEntity.toItem(): PokemonItem = PokemonItem(id, name, imageUrl)