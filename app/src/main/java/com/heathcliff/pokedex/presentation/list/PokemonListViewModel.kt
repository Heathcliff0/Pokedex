package com.heathcliff.pokedex.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heathcliff.pokedex.domain.PokemonRepository
import com.heathcliff.pokedex.presentation.list.adapter.toItem
import kotlinx.coroutines.launch

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _viewStateLiveData = MutableLiveData<PokemonListViewState>()
    fun viewState(): LiveData<PokemonListViewState> = _viewStateLiveData

    fun loadData() {
        _viewStateLiveData.value = PokemonListViewState.Loading

        viewModelScope.launch {
            try {
                _viewStateLiveData.value =
                    PokemonListViewState.Data(repository.getPokemonList().map { it.toItem() })
            } catch (t: Throwable) {
                _viewStateLiveData.value = PokemonListViewState.Error
            }
        }
    }
}