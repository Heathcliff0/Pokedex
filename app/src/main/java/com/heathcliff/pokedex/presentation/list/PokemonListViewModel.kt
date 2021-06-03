package com.heathcliff.pokedex.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heathcliff.pokedex.domain.PokemonRepository
import com.heathcliff.pokedex.presentation.list.adapter.PokemonItem
import kotlinx.coroutines.launch

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {

    var pokemonExistsLiveData = MutableLiveData<Pair<Boolean, String>>()
    var toastTriggerLiveData = MutableLiveData<Boolean>()

    var filterState = FilterState.ALL
    val loadTrigger = MutableLiveData(false)

    private val _viewStateLiveData = MutableLiveData<PokemonListViewState>()
    fun viewState(): LiveData<PokemonListViewState> = _viewStateLiveData

    private var items: List<PokemonItem> = emptyList()

    private var currentOffset = 0
    fun currentOffset() = currentOffset

    var currentGen = 1
    var currentType = 1

    fun loadFirstPokemons() {
        _viewStateLiveData.value = PokemonListViewState.Loading
        currentOffset = 0

        viewModelScope.launch {
            try {
                when (filterState) {
                    FilterState.ALL -> {
                        items = emptyList()
                        items += repository.getPokemonList(0)
                    }
                    FilterState.GENERATION -> {
                        items = emptyList()
                        items += repository.getPokemonGenerationList(currentGen)
                    }
                    FilterState.TYPE -> {
                        items = emptyList()
                        items += repository.getPokemonTypeList(currentType)
                    }
                }
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
                when (filterState) {
                    FilterState.ALL -> {
                        items += repository.getPokemonList(currentOffset)
                    }
                }
                _viewStateLiveData.value = PokemonListViewState.Data(items)
            } catch (t: Throwable) {
                _viewStateLiveData.value = PokemonListViewState.Error
            }
        }
    }

    fun checkPokemonExistence(id: String) {
        viewModelScope.launch {
            try {
                repository.checkPokemonExistenceById(id.lowercase())
                pokemonExistsLiveData.value = true to id
            } catch (t: Throwable) {
                pokemonExistsLiveData.value = false to ""
                toastTriggerLiveData.value = true
            }
        }
    }
}

enum class FilterState {
    ALL, GENERATION, TYPE
}