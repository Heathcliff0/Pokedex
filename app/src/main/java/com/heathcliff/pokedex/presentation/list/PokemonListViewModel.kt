package com.heathcliff.pokedex.presentation.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.heathcliff.pokedex.di.Injector
import com.heathcliff.pokedex.presentation.list.adapter.toItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PokemonListViewModel : ViewModel() {
    //private val repository: PokemonRepository = MockPokemonRepository()
    private val repository = Injector.providePokemonRepository()

    private var disposable: Disposable? = null

    private val _viewStateLiveData = MutableLiveData<PokemonListViewState>()
    fun viewState(): LiveData<PokemonListViewState> = _viewStateLiveData

    fun loadData() {
        _viewStateLiveData.value = PokemonListViewState.LoadingState

        disposable = repository.getPokemonList()
                .map { items -> items.map { it.toItem() } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            _viewStateLiveData.value = PokemonListViewState.ContentState(it)
                        },
                        {
                            Log.d("ViewModel", "Error is", it)
                            _viewStateLiveData.value = PokemonListViewState.ErrorState("Oops, something went wrong. Try restarting the app.")
                        }
                )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
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