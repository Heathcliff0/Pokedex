package com.heathcliff.pokedex.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.heathcliff.pokedex.di.Injector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class PokemonDetailsViewModel: ViewModel() {
    private val repository = Injector.providePokemonRepository()

    private var disposable: Disposable? = null

    private val _viewStateLiveData = MutableLiveData<PokemonDetailsViewState>()
    fun viewState(): LiveData<PokemonDetailsViewState> = _viewStateLiveData

    fun loadPokemonById(id: String) {
        _viewStateLiveData.value = PokemonDetailsViewState.Loading

        disposable = repository.getPokemonById(id)
            .delay(1,TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ pokemon ->
                _viewStateLiveData.value = PokemonDetailsViewState.Data(
                    name = pokemon.name,
                    imageUrl = pokemon.imageUrl,
                    abilities = pokemon.abilities
                )
            }, {
                _viewStateLiveData.value = PokemonDetailsViewState.Error("Failed to load pokemon with id=$id")
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}