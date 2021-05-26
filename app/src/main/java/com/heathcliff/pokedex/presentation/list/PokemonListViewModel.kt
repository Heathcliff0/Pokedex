package com.heathcliff.pokedex.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heathcliff.pokedex.di.Injector
import com.heathcliff.pokedex.presentation.list.adapter.toItem
import kotlinx.coroutines.launch

class PokemonListViewModel : ViewModel() {
    //private val repository: PokemonRepository = MockPokemonRepository()
    private val repository = Injector.providePokemonRepository()

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

    /*    fun showData(pokemons: List<PokemonEntity>) {
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
    }*/
}