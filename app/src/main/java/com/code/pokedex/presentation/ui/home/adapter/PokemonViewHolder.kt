package com.code.pokedex.presentation.ui.home.adapter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.card.MaterialCardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.code.pokedex.R
import com.code.pokedex.domain.model.Pokemon
import com.code.pokedex.framework.utils.Utils

/**
 * View Holder for a [Pokemon] RecyclerView list item.
 */
class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val number: TextView = view.findViewById(R.id.tNumero)
    private val name: TextView = view.findViewById(R.id.tNombre)
    private val image: ImageView = view.findViewById(R.id.imagePokemon)
    private val typeContainer: LinearLayout = view.findViewById(R.id.layoutTypes)
    private val card: MaterialCardView = view.findViewById(R.id.cardPokemon)

    private var pokemon: Pokemon? = null

    init {
        card.setOnClickListener {
            Toast.makeText(view.context, pokemon?.name,Toast.LENGTH_LONG).show()
            pokemon?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(pokemon: Pokemon) {
        if (pokemon == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
        } else {
            showData(pokemon)
        }
    }

    private fun showData(pokemon: Pokemon) {
        this.pokemon = pokemon
        number.text = "N° ${Utils.formatId(pokemon.id)}"
        name.text = pokemon.name.uppercase()
        image.loadUrl(pokemon.url)
        typeContainer.removeAllViews()
        pokemon.types.map {
            val type = CircleView(image.context)
            type.setColor(getColorByType(it))
            type.layoutParams = ViewGroup.LayoutParams(40, 40)
            val space = CircleView(image.context)
            space.setColor(Color.TRANSPARENT)
            space.layoutParams = ViewGroup.LayoutParams(10, 10)
            typeContainer.addView(type)
            typeContainer.addView(space)
        }
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

    fun getColorByType(type: String): Int = when(type) {
        "normal" -> Color.parseColor(TypeNormal)
        "fighting" -> Color.parseColor(TypeFighting)
        "flying" -> Color.parseColor(TypeFlying)
        "poison" -> Color.parseColor(TypePoison)
        "ground" -> Color.parseColor(TypeGround)
        "rock" -> Color.parseColor(TypeRock)
        "bug" -> Color.parseColor(TypeBug)
        "ghost" -> Color.parseColor(TypeGhost)
        "steel" -> Color.parseColor(TypeSteel)
        "fire" -> Color.parseColor(TypeFire)
        "water" -> Color.parseColor(TypeWater)
        "grass" -> Color.parseColor(TypeGrass)
        "electric" -> Color.parseColor(TypeElectric)
        "psychic" -> Color.parseColor(TypePsychic)
        "ice" -> Color.parseColor(TypeIce)
        "dragon" -> Color.parseColor(TypeDragon)
        "dark" -> Color.parseColor(TypeDark)
        "fairy" -> Color.parseColor(TypeFairy)
        "shadow" -> Color.parseColor(TypeShadow)
        "unknown" -> Color.TRANSPARENT
        else -> Color.TRANSPARENT
    }
    val TypeNormal = "#FFA8A77A"
    val TypeFire = "#FFEE8130"
    val TypeWater = "#FF6390F0"
    val TypeElectric = "#FFF7D02C"
    val TypeGrass = "#FF7AC74C"
    val TypeIce = "#FF96D9D6"
    val TypeFighting = "#FFC22E28"
    val TypePoison = "#FFA33EA1"
    val TypeGround = "#FFE2BF65"
    val TypeFlying = "#FFA98FF3"
    val TypePsychic = "#FFF95587"
    val TypeBug = "#FFA6B91A"
    val TypeRock = "#FFB6A136"
    val TypeGhost = "#FF735797"
    val TypeDragon = "#FF6F35FC"
    val TypeDark = "#FF705746"
    val TypeSteel = "#FFB7B7CE"
    val TypeFairy = "#FFD685AD"
    val TypeShadow = "#625B71"
    val TypeUnknow = ""
}
