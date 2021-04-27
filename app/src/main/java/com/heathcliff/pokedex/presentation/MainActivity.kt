package com.heathcliff.pokedex.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.heathcliff.pokedex.presentation.list.PokemonListFragment

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, PokemonListFragment())
            .commit()
    }
}