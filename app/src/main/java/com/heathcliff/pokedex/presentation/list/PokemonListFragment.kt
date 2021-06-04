package com.heathcliff.pokedex.presentation.list

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.appcompat.widget.SearchView
import androidx.core.view.forEach
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

    private var genFilterMenuItems: MutableList<Int> = mutableListOf()
    private var typeFilterMenuItems: MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        // ========== Observers ==========

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

        viewModel.pokemonExistsLiveData.observe(viewLifecycleOwner, {
            if (it.first) {
                requireView().findNavController().navigate(
                    PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(
                        it.second.lowercase()
                    )
                )
                viewModel.pokemonExistsLiveData.value = false to ""
            }
        })

        viewModel.toastTriggerLiveData.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(activity, "Oops. Not found...", Toast.LENGTH_SHORT).show()
                viewModel.toastTriggerLiveData.value = false
            }
        })

        return binding.root
    }

    // ========== Options Menu ==========

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_pokedex, menu)

        menu.findItem(R.id.byGenerationFilter).subMenu.forEach { genFilterMenuItems.add(it.itemId) }
        menu.findItem(R.id.byTypeFilter).subMenu.forEach { typeFilterMenuItems.add(it.itemId) }

        val searchView = menu.findItem(R.id.searchOption).actionView as SearchView
        searchView.queryHint = "Search Pokemon..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.checkPokemonExistence(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.AllFilter -> {
                viewModel.filterState = FilterState.ALL
                viewModel.loadFirstPokemons()
                true
            }
            in genFilterMenuItems -> {
                loadGen(genFilterMenuItems.indexOf(item.itemId) + 1)
                return true
            }
            in typeFilterMenuItems -> {
                loadType(typeFilterMenuItems.indexOf(item.itemId) + 1)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
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

    private fun loadGen(gen: Int) {
        viewModel.currentGen = gen
        viewModel.filterState = FilterState.GENERATION
        viewModel.loadFirstPokemons()
    }

    private fun loadType(type: Int) {
        viewModel.currentType = type
        viewModel.filterState = FilterState.TYPE
        viewModel.loadFirstPokemons()
    }

    private fun showData(items: List<PokemonItem>) {
        adapter.submitList(items)
        binding.loadingNextPokemonsBar.visibility = View.GONE
        isAvailableToLoad = true
    }

}