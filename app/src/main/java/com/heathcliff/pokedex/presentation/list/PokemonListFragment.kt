package com.heathcliff.pokedex.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.heathcliff.pokedex.R
import com.heathcliff.pokedex.databinding.FragmentPokemonListBinding
import com.heathcliff.pokedex.presentation.details.PokemonDetailsFragment
import com.heathcliff.pokedex.presentation.list.adapter.DisplayableItem
import com.heathcliff.pokedex.presentation.list.adapter.MainAdapter
import com.heathcliff.pokedex.utils.ItemViewTypes

class PokemonListFragment : Fragment() {
    private val viewModel = PokemonListViewModel()
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding: FragmentPokemonListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pokemon_list, container, false)

        initRecyclerView(binding)

        viewModel.viewState().observe(this, Observer { state ->
            when (state) {
                is PokemonListViewState.LoadingState -> showProgress()
                is PokemonListViewState.ErrorState -> showError(state.errorMessage)
                is PokemonListViewState.ContentState -> showData(state.items)
            }
        })

        viewModel.loadData()

        return binding.root
    }

    // ========== Functions ==========

    private fun initRecyclerView(binding: FragmentPokemonListBinding) {
        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    ItemViewTypes.ITEM_VIEW_TYPE_BANNER -> 2
                    ItemViewTypes.ITEM_VIEW_TYPE_POKEMON -> 1
                    else -> throw IllegalStateException("Unknown item view type.")
                }
            }
        }

        adapter = MainAdapter(
            onItemClicked = {
                val safeContext = context
                if (safeContext != null) {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(android.R.id.content, PokemonDetailsFragment())
                        ?.addToBackStack(null)
                        ?.commit()
                }
            }
        )

        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
    }

    private fun showProgress() {
        Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
    }

    private fun showData(items: List<DisplayableItem>) {
        adapter.setDisplayableItemsList(items)
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
    }
}

