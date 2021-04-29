package com.heathcliff.pokedex.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.heathcliff.pokedex.R
import com.heathcliff.pokedex.databinding.FragmentPokemonDetailsBinding

class PokemonDetailsFragment: Fragment(R.layout.fragment_pokemon_details) {
    private lateinit var binding: FragmentPokemonDetailsBinding
    private val viewModel = PokemonDetailsViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val args = PokemonDetailsFragmentArgs.fromBundle(requireArguments())
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pokemon_details, container, false)

        loadPokemonData(args.pokemonId)

        return binding.root
    }

    private fun loadPokemonData(id: String){
        viewModel.loadPokemonById(id)

        viewModel.viewState().observe(viewLifecycleOwner, Observer{
            when(it){
                is PokemonDetailsViewState.Loading -> {
                    binding.pokemonDetailsGroup.isVisible = false
                    binding.pokemonDetailsProgressBar.isVisible = true
                    binding.pokemonDetailsErrorImage.isVisible = false
                }
                is PokemonDetailsViewState.Data -> {
                    binding.pokemonDetailsGroup.isVisible = true
                    binding.pokemonDetailsProgressBar.isVisible = false
                    binding.pokemonDetailsErrorImage.isVisible = false

                    showData(it)
                }
                is PokemonDetailsViewState.Error -> {
                    binding.pokemonDetailsGroup.isVisible = false
                    binding.pokemonDetailsProgressBar.isVisible = false
                    binding.pokemonDetailsErrorImage.isVisible = true
                }
            }
        })
    }

    fun showData(state: PokemonDetailsViewState.Data){
        binding.pokemonDetailsName.text = state.name
        binding.pokemonDetailsAbilities.text = state.abilities.joinToString(separator = "\n"){it}

        Glide.with(binding.pokemonDetailsImageView.context)
            .load(state.imageUrl.toUri().buildUpon().scheme("https").build())
            .into(binding.pokemonDetailsImageView)
    }
}