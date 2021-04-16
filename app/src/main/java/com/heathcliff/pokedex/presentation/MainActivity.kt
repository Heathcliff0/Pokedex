package com.heathcliff.pokedex.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.heathcliff.pokedex.R
import com.heathcliff.pokedex.databinding.ActivityMainBinding
import com.heathcliff.pokedex.presentation.adapter.MainAdapter

class MainActivity : AppCompatActivity() {

    val list: List<String> = listOf("One", "Two", "Three", "Four", "Five")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel = MainViewModel()
        val adapter = MainAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        viewModel.getPokemonList().observe(this, Observer {
            adapter.setDisplayableItemsList(it)
        })

        viewModel.loadData()
    }
}