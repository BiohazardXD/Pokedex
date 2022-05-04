/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.code.pokedex.presentation.ui.home.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.code.pokedex.R
import com.code.pokedex.domain.model.Pokemon

/**
 * View Holder for a [Pokemon] RecyclerView list item.
 */
class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.tNombre)
    private val image: ImageView = view.findViewById(R.id.imagePokemon)

    private var pokemon: Pokemon? = null

    init {
        view.setOnClickListener {
            pokemon?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(pokemon: Pokemon?) {
        if (pokemon == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
        } else {
            showRepoData(pokemon)
        }
    }

    private fun showRepoData(pokemon: Pokemon) {
        this.pokemon = pokemon
        name.text = pokemon.name
        image.loadUrl("https://assets.pokemon.com/assets/cms2/img/pokedex/full/${pokemon.url}.png")
    }

    companion object {
        fun create(parent: ViewGroup): PokemonViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pokemon_view_item, parent, false)
            return PokemonViewHolder(view)
        }
    }

    fun ImageView.loadUrl(url: String) {
        Glide.with(context).load(url).into(this)
    }
}
