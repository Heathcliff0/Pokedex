package com.heathcliff.pokedex.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heathcliff.pokedex.domain.PokemonRepository
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _viewStateLiveData = MutableLiveData<PokemonDetailsViewState>()
    fun viewState(): LiveData<PokemonDetailsViewState> = _viewStateLiveData

    fun loadPokemonById(id: String) {
        _viewStateLiveData.value = PokemonDetailsViewState.Loading

        viewModelScope.launch {
            try {
                val pokemon = repository.getPokemonById(id)
                _viewStateLiveData.value = PokemonDetailsViewState.Data(
                    name = pokemon.name,
                    imageUrl = pokemon.imageUrl,
                    abilities = pokemon.abilities,
                    stats = pokemon.stats,
                    types = pokemon.types,
                    weight = pokemon.weight,
                    height = pokemon.height
                )
            } catch (t: Throwable) {
                _viewStateLiveData.value =
                    PokemonDetailsViewState.Error("Failed to load pokemon with id=$id")
            }
        }
    }
}