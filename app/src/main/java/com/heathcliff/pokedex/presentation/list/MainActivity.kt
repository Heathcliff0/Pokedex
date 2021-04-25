package com.heathcliff.pokedex.presentation.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.heathcliff.pokedex.R
import com.heathcliff.pokedex.databinding.ActivityMainBinding
import com.heathcliff.pokedex.presentation.list.adapter.DisplayableItem
import com.heathcliff.pokedex.presentation.list.adapter.MainAdapter
import com.heathcliff.pokedex.utils.ItemViewTypes

class MainActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initRecyclerView(binding)

        viewModel.viewState().observe(this, Observer { state ->
            when (state) {
                is MainViewState.LoadingState -> showProgress()
                is MainViewState.ErrorState -> showError(state.errorMessage)
                is MainViewState.ContentState -> showData(state.items)
            }
        })

        viewModel.loadData()
    }


    // ========== Functions ==========

    private fun initRecyclerView(binding: ActivityMainBinding) {
        val manager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
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
            onItemClicked = {id ->
                Toast.makeText(this, "Pokemon selected with id=$id", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
    }

    private fun showProgress() {
        Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
    }

    private fun showData(items: List<DisplayableItem>) {
        adapter.setDisplayableItemsList(items)
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }
}
