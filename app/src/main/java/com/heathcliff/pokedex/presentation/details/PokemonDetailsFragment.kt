package com.heathcliff.pokedex.presentation.details

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.heathcliff.pokedex.R
import com.heathcliff.pokedex.databinding.FragmentPokemonDetailsBinding
import com.heathcliff.pokedex.utils.setStats

class PokemonDetailsFragment : Fragment(R.layout.fragment_pokemon_details) {
    private lateinit var binding: FragmentPokemonDetailsBinding
    private val viewModel = PokemonDetailsViewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val args = PokemonDetailsFragmentArgs.fromBundle(requireArguments())
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_pokemon_details, container, false)

        loadPokemonData(args.pokemonId)

        return binding.root
    }

    private fun loadPokemonData(id: String) {
        viewModel.loadPokemonById(id)

        viewModel.viewState().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is PokemonDetailsViewState.Loading -> {
                    binding.pokemonDetailsGroup.isVisible = false
                    binding.pokemonDetailsProgressBar.isVisible = true
                    binding.pokemonDetailsErrorImage.isVisible = false
                }
                is PokemonDetailsViewState.Data -> {
                    binding.pokemonDetailsGroup.isVisible = true
                    binding.pokemonDetailsProgressBar.isVisible = false
                    binding.pokemonDetailsErrorImage.isVisible = false

                    showData(state)
                }
                is PokemonDetailsViewState.Error -> {
                    binding.pokemonDetailsGroup.isVisible = false
                    binding.pokemonDetailsProgressBar.isVisible = false
                    binding.pokemonDetailsErrorImage.isVisible = true
                }
            }
        })
    }

    fun showData(state: PokemonDetailsViewState.Data) {
        binding.pokemonName.text = state.name
        binding.pokemonAbilities.text = state.abilities.joinToString(separator = ", ") { it }

        binding.weightText.text = "${state.weight.toFloat() / 10} KG"
        binding.heightText.text = "${state.height.toFloat() / 10} M"

        binding.pokemonTypesText.text = state.types.joinToString(separator = ", ") { it }

        setStats(binding, state.stats)

        Glide.with(binding.pokemonImage.context)
                .asBitmap()
                .load(state.imageUrl.toUri().buildUpon().scheme("https").build())
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val color = Palette.from(resource).generate().getMutedColor(535353)
                        binding.pokemonImage.setImageBitmap(resource)
                        binding.circlePokemon.background.setTint(color)
                        binding.hpBar.progressBackgroundColor = color
                        binding.atkBar.progressBackgroundColor = color
                        binding.defBar.progressBackgroundColor = color
                        binding.spdBar.progressBackgroundColor = color
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
    }
}