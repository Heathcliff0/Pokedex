package com.heathcliff.pokedex.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heathcliff.pokedex.domain.PokemonRepository
import com.heathcliff.pokedex.presentation.list.adapter.PokemonItem
import com.heathcliff.pokedex.presentation.list.adapter.toItem
import kotlinx.coroutines.launch

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _viewStateLiveData = MutableLiveData<PokemonListViewState>()
    fun viewState(): LiveData<PokemonListViewState> = _viewStateLiveData

    private var items: List<PokemonItem> = emptyList()
    private var currentOffset = 0
    fun currentOffset() = currentOffset

    fun loadFirstPokemons() {
        _viewStateLiveData.value = PokemonListViewState.Loading

        viewModelScope.launch {
            try {
                items += (repository.getPokemonList(0).map { it.toItem() })
                _viewStateLiveData.value = PokemonListViewState.Data(items)
            } catch (t: Throwable) {
                _viewStateLiveData.value = PokemonListViewState.Error
            }
        }
    }

    fun loadNextPokemons() {
        currentOffset += 20

        viewModelScope.launch {
            try {
                items += (repository.getPokemonList(currentOffset).map { it.toItem() })
                _viewStateLiveData.value = PokemonListViewState.Data(items)
            } catch (t: Throwable) {
                _viewStateLiveData.value = PokemonListViewState.Error
            }
        }
    }
}