package com.heathcliff.pokedex.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.heathcliff.pokedex.R

private val ITEM_VIEW_TYPE_POKEMON = 0
private val ITEM_VIEW_TYPE_BANNER = 1

class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val displayableItems = listOf<DisplayableItem>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            ITEM_VIEW_TYPE_POKEMON -> PokemonViewHolder.from(parent)
            ITEM_VIEW_TYPE_BANNER -> BannerViewHolder.from(parent)
            else -> throw IllegalStateException("Unknown item view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = displayableItems[position]
        when (holder) {
            is PokemonViewHolder -> holder.bind(item as PokemonItem)
            is BannerViewHolder -> holder.bind(item as BannerItem)
        }
    }

    override fun getItemCount(): Int = displayableItems.size

    override fun getItemViewType(position: Int): Int {
        return when (displayableItems[position]) {
            is PokemonItem -> ITEM_VIEW_TYPE_POKEMON
            is BannerItem -> ITEM_VIEW_TYPE_BANNER
            else -> throw IllegalStateException("Unknown item view type")
        }
    }

    fun setDisplayableItemsList(items: List<DisplayableItem>) {
        displayableItems.clear()
        displayableItems.addAll(items)
        notifyDataSetChanged()
    }
}

// =============== PokemonViewHolder ===============

class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val nameTextView: TextView = view.findViewById(R.id.pokemonItemNameText)
    private val pokemonImageView: ImageView = view.findViewById(R.id.pokemonItemImageView)

    fun bind(item: PokemonItem) {
        nameTextView.text = item.name

        Glide.with(pokemonImageView.context)
                .load(item.imageUrl.toUri().buildUpon().scheme("https").build())
                .into(pokemonImageView)
    }

    companion object {
        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            return PokemonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false))
        }
    }
}

// =============== BannerViewHolder ===============

class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val bannerItem = itemView.findViewById<TextView>(R.id.banner_item)

    fun bind(item: BannerItem) {
        bannerItem.text = item.text
    }

    companion object {
        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            return BannerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.banner_item, parent, false))
        }
    }
}