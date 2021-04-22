package com.heathcliff.pokedex.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.heathcliff.pokedex.data.NetworkPokemonRepository
import com.heathcliff.pokedex.data.network.createPokemonApiService
import com.heathcliff.pokedex.domain.PokemonEntity
import com.heathcliff.pokedex.domain.PokemonRepository
import com.heathcliff.pokedex.presentation.adapter.BannerItem
import com.heathcliff.pokedex.presentation.adapter.DisplayableItem
import com.heathcliff.pokedex.presentation.adapter.toItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    //private val repository: PokemonRepository = MockPokemonRepository()
    private val repository: PokemonRepository = NetworkPokemonRepository(
            api = createPokemonApiService()
    )

    private var disposable: Disposable? = null

    private val _viewStateLiveData = MutableLiveData<MainViewState>()
    fun viewState(): LiveData<MainViewState> = _viewStateLiveData

    fun loadData() {
        _viewStateLiveData.value = MainViewState.LoadingState

        disposable = repository.getPokemonList()
                .map { items -> items.map { it.toItem() } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            _viewStateLiveData.value = MainViewState.ContentState(it)
                        },
                        {
                            Log.d("ViewModel", "Error is", it)
                            _viewStateLiveData.value = MainViewState.ErrorState("Oops, something went wrong")
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