package com.heathcliff.pokedex.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.heathcliff.pokedex.R
import com.heathcliff.pokedex.databinding.ActivityMainBinding
import com.heathcliff.pokedex.presentation.adapter.MainAdapter
import com.heathcliff.pokedex.utils.ItemViewTypes

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel = MainViewModel()
        val adapter = MainAdapter()

        val manager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    ItemViewTypes.ITEM_VIEW_TYPE_BANNER -> 2
                    ItemViewTypes.ITEM_VIEW_TYPE_POKEMON -> 1
                    else -> throw IllegalStateException("Unknown item view type")
                }
            }
        }

        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        viewModel.getPokemonList().observe(this, Observer {
            adapter.setDisplayableItemsList(it)
        })

        viewModel.loadData()
    }
}
