package com.heathcliff.pokedex.presentation.list.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.heathcliff.pokedex.R
import kotlin.coroutines.coroutineContext

class PokemonListAdapter(
    private val onItemClicked: (id: String) -> Unit
) : ListAdapter<PokemonItem, PokemonViewHolder>(PokemonItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder.from(parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

// =============== PokemonViewHolder ===============

class PokemonViewHolder(view: View, val onItemClicked: (id: String) -> Unit) :
    RecyclerView.ViewHolder(view) {

    private val nameTextView: TextView = view.findViewById(R.id.pokemonItemNameText)
    private val pokemonImageView: ImageView = view.findViewById(R.id.pokemonItemImageView)

    fun bind(item: PokemonItem) {
        nameTextView.text = item.name

        Glide.with(pokemonImageView.context)
            .asBitmap()
            .load(item.imageUrl.toUri().buildUpon().scheme("https").build())
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    pokemonImageView.setImageBitmap(resource)
                    itemView.background.setTint(
                        Palette.from(resource).maximumColorCount(24).generate()
                            .getMutedColor(535353)
                    )
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            }
            )

        itemView.setOnClickListener {
            onItemClicked(item.id)
        }
    }

    companion object {
        fun from(parent: ViewGroup, onItemClicked: (id: String) -> Unit): PokemonViewHolder {
            return PokemonViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false),
                onItemClicked
            )
        }
    }
}
