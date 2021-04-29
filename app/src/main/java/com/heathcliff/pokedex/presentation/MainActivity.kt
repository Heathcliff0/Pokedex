package com.heathcliff.pokedex.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.heathcliff.pokedex.R
import com.heathcliff.pokedex.presentation.list.PokemonListFragment

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, PokemonListFragment())
            .commit()*/
    }
}