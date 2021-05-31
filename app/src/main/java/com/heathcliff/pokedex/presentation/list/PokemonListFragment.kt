package com.heathcliff.pokedex.presentation.list

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heathcliff.pokedex.R
import com.heathcliff.pokedex.databinding.FragmentPokemonListBinding
import com.heathcliff.pokedex.presentation.list.adapter.PokemonItem
import com.heathcliff.pokedex.presentation.list.adapter.PokemonListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonListFragment : Fragment(R.layout.fragment_pokemon_list) {
    private val viewModel: PokemonListViewModel by viewModel()
    private lateinit var adapter: PokemonListAdapter
    private lateinit var binding: FragmentPokemonListBinding
    private var isAvailableToLoad = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadFirstPokemons()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pokemon_list, container, false)

        initRecyclerView(binding)

        viewModel.viewState().observe(viewLifecycleOwner, { state ->
            when (state) {
                is PokemonListViewState.Loading -> {
                    binding.pokemonListProgressBar.isVisible = true
                    binding.pokemonListErrorImage.isVisible = false
                }
                is PokemonListViewState.Data -> {
                    binding.recyclerView.isVisible = true
                    binding.pokemonListProgressBar.isVisible = false
                    binding.pokemonListErrorImage.isVisible = false

                    showData(state.items)
                }
                is PokemonListViewState.Error -> {
                    binding.recyclerView.isVisible = false
                    binding.pokemonListProgressBar.isVisible = false
                    binding.pokemonListErrorImage.isVisible = true
                }
            }
        })

        return binding.root
    }

    // ========== Functions ==========

    private fun initRecyclerView(binding: FragmentPokemonListBinding) {
        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)

        adapter = PokemonListAdapter(
            onItemClicked = {
                view?.findNavController()?.navigate(
                    PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(
                        it
                    )
                )
            })

        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (viewModel.currentOffset() < 80 && isAvailableToLoad) {
                    if (((recyclerView.layoutManager as GridLayoutManager)
                            .findLastCompletelyVisibleItemPosition() == recyclerView.adapter!!.itemCount - 1)
                    ) {
                        isAvailableToLoad = false
                        binding.loadingNextPokemonsBar.visibility = View.VISIBLE
                        viewModel.loadNextPokemons()
                    }
                }
            }
        })
    }

    private fun showData(items: List<PokemonItem>) {
        adapter.submitList(items)
        binding.loadingNextPokemonsBar.visibility = View.GONE
        isAvailableToLoad = true
    }
}